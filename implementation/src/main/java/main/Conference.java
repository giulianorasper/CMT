package main;

import agenda.Agenda;
import agenda.AgendaManagement;
import agenda.DB_AgendaManagement;
import database.*;
import document.DB_DocumentManagement;
import document.Document;
import document.DocumentManagement;
import request.DB_RequestManagement;
import request.Request;
import request.RequestManagement;
import user.*;
import utils.Generator;
import utils.Generator_Imp;
import utils.Log;
import utils.Operation;
import utils.OperationResponse;
import utils.Pair;
import voting.DB_VotingManagement;
import voting.Voting;
import voting.VotingManagement;
import voting.VotingObserver;
import voting.VotingStatus;

import java.io.File;
import java.nio.file.Path;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Conference implements UserManagement, VotingManagement, RequestManagement, DocumentManagement, AgendaManagement, VotingObserver {

    //Creates a clean conference (for debugging)
    public Conference(){
        this (  "Test",
                "Team 23",
                System.currentTimeMillis(),
                System.currentTimeMillis() + 1000*60*60,
                new Agenda(""),
                new HashMap<Integer, Admin>(),
                new HashMap<Integer, Voting>(),
                new HashMap<String, Path>(),
                "./docs",
                new HashSet<String>(),
                new HashMap<Integer, Request>(),
                null
        );

        this.addAdmin(new Admin("test", "test", "test", "test", "test", "test", 0));
        System.out.println(this.getAttendeePassword(0).second());
    }


    public Conference(String name, String organizer, long startsAt, long endsAt, Agenda agenda, HashMap<Integer, Admin> admins, HashMap<Integer,Voting> votings, HashMap<String,Path> documents, String  documentsPath, Set<String> adminTokens, HashMap<Integer,Request> requests, Voting activeVoting) {
        this.name = name;
        this.organizer = organizer;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.agenda = agenda;
        this.votings = votings;
        this.documents = documents;
        this.requests = requests;
        this.activeVoting = activeVoting;
        this.documentsPath = documentsPath;

        this.admins = admins;

        adminTokens.forEach(f -> this.adminTokens.put(f, true));
        DB_AgendaManager db_agendaManagement = new DB_AgendaManager("");
        agenda.register(db_agendaManagement);
        File f = new File(documentsPath);
        if(!f.exists() && !f.mkdir()){
            throw new IllegalArgumentException("Could not create directory " + documentsPath);
        }
        if(f.exists() && !f.isDirectory()){
            throw new IllegalArgumentException("Could not create directory " + documentsPath +" , because a file with that name already exists");
        }
        if(f.exists() && f.isDirectory()){
            //todo read files from directory
        }
    }

    //Conference Data
    private String name;

    private String organizer;

    private long startsAt;
    private long endsAt;

    private Generator gen = new Generator_Imp();

    private String  documentsPath ;

    private Agenda agenda;
    private HashMap<Integer,Voting> votings;
    private HashMap<String, Path> documents;
    private HashMap<Integer,Admin> admins;
    private HashMap<Integer,Request> requests;
    private Voting activeVoting;
    private HashMap<String, Boolean> adminTokens; // a map backed Set

    //Database System //TODO add urls
    private DB_DocumentManagement db_documentManagement = new DB_DocumentManager("./testdb/testdb.db");
    private DB_UserManagement db_userManagement = new DB_UserManager("./testdb/testdb.db");
    private DB_RequestManagement db_requestManagement = new DB_RequestManager("./testdb/testdb.db");
    private DB_VotingManager db_votingManagement = new DB_VotingManager("./testdb/testdb.db");



    //Locks - always take in this order !!!
    private Lock adminLock = new ReentrantLock();
    private Lock attendeeLock = new ReentrantLock();
    private Lock votingLock = new ReentrantLock();
    private Lock requestLock = new ReentrantLock();
    private Lock documentsLock = new ReentrantLock();

    /****************** The Request Management Interface *********/

    @Override
    public void addRequest( Request request) {

        try {
            requestLock.lock();
            if(requests.containsKey(request.ID)){
                throw new IllegalArgumentException();
            }
            if(!db_requestManagement.addRequest(request)){
                throw new IllegalArgumentException();
            }
            requests.put(request.ID, request);
        }
        finally {
            requestLock.unlock();
        }

    }

    @Override
    public Request getRequest( int ID) {
        try {
            requestLock.lock();
            return requests.get(ID);
        }
        finally {
            requestLock.unlock();
        }
    }

    @Override
    public List<Request> getAllRequests() {
        try {
            requestLock.lock();
            return new ArrayList<>(requests.values());
        }
        finally {
            requestLock.unlock();
        }
    }

    /****************** The User Management Interface *********/
    @Override
    public void addAdmin(Admin a) {
        try{
            adminLock.lock();
            if(!db_userManagement.addAdmin(a, gen.generatePassword(), gen.generateToken())){
                throw new IllegalArgumentException("Database addition failed");
            }
            admins.put(a.getID(), a);
        }
        finally {
            adminLock.unlock();
        }
    }

    @Override
    public List<Admin> getAllAdmins() {
        try {
            adminLock.lock();
            return new ArrayList<>(admins.values());
        }
        finally {
            adminLock.unlock();
        }
    }

    @Override
    public Admin getAdminPersonalData(int ID) {
        try {
            adminLock.lock();
            return admins.get(ID);
        }
        finally {
            adminLock.unlock();
        }
    }

    @Override
    public void removeAdmin(int ID) {
        try {
            adminLock.lock();
            if (admins.get(ID) == null) {
                throw new IllegalArgumentException("Admin not found");
            }
            if(!db_userManagement.removeUser(ID)){
                throw new IllegalArgumentException("Admin can not be removed for unknown reasons");
            }
        }
        finally {
            adminLock.unlock();
        }
    }

    @Override
    public void logoutAdmin(int ID) {
        try{
            adminLock.lock();
            if(admins.get(ID) == null){
                throw new IllegalArgumentException("Admin not found");
            }
            else{
                admins.get(ID).logout();
            }
            if(!(db_userManagement.logoutUser(ID))){
                throw new IllegalArgumentException("Admin can not be logged out for unknown reasons");
            }
        }
        finally {
            adminLock.unlock();
        }
    }

    @Override
    public void editAdmin(int ID, Admin a) {
        try {
            adminLock.lock();
            if(!admins.containsKey(ID)){
                throw new IllegalArgumentException("Admin not found");
            }
            if(!db_userManagement.editAdmin(a)){
                throw new IllegalArgumentException("Admin can not be edited for unknown reasons");
            }
            admins.replace(ID, a);
        }
        finally {
            adminLock.unlock();
        }
    }


    @Override
    public void addAttendee( Attendee a) {
        try{
            attendeeLock.lock();
            if(!db_userManagement.addAttendee(a, gen.generatePassword(), gen.generateToken())){
                throw new IllegalArgumentException("Attendee can not be edited for unknown reasons");
            }

        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public List<Attendee> getAllAttendees() {
        try{
            attendeeLock.lock();
            return db_userManagement.getAllAttendees();
        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public Attendee getAttendeeData(int userID) {
        try{
            attendeeLock.lock();
            return db_userManagement.getAttendeeData(userID);
        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public void removeAttendee( int userID) {
        try{
            attendeeLock.lock();
            if(!db_userManagement.removeUser(userID)){
                throw new IllegalArgumentException("Admin can not be removed for unknown reasons");
            }

        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public void logoutAttendee( int userID) {
        try{
            attendeeLock.lock();
            if(!db_userManagement.logoutUser(userID)){
                throw new IllegalArgumentException("Attendee can not be logged out for unknown reasons");
            }
        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public void editAttendee(Attendee attendee) {
        try{
            attendeeLock.lock();
           if(!db_userManagement.editAttendee(attendee)){
               throw new IllegalArgumentException("Attendee could not be edited for unknown reasons");
            }
        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public void generateNewAttendeePassword( int userID) {
        try{
            attendeeLock.lock();
            if(!db_userManagement.storeNewPassword(userID, gen.generatePassword())){
                throw new IllegalArgumentException();
            }
        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public void generateNewAttendeeToken( int userID) {
        try{
            attendeeLock.lock();
            if(!db_userManagement.storeNewToken(userID, gen.generateToken())){
                throw new IllegalArgumentException();
            }
        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public void generateAllMissingAttendeePasswords(String adminToken) {
        try{
            attendeeLock.lock();
            boolean success = true;
            for (Pair<User, String> p:db_userManagement.getAllPasswords()) {
                if(p.second() == null){
                    success = success && db_userManagement.storeNewPassword(p.first().getID(), gen.generatePassword());
                }
            }

            if(!success){
                throw new IllegalArgumentException();
            }
        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public Pair<User, String> getAttendeePassword( int userID) {
        try{
            attendeeLock.lock();
            for (Pair<User, String> p:db_userManagement.getAllPasswords()) {
                if(p.first().getID() == userID){
                    return  p;
                }
            }
            throw new IllegalArgumentException();
        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public List<Pair<User, String>> getAllAttendeePasswords() {
        try{
            attendeeLock.lock();
            return db_userManagement.getAllPasswords();
        }
        finally {
            attendeeLock.unlock();
        }
    }

    @Override
    public boolean logoutAllAttendees() {
        try{
            attendeeLock.lock();
            boolean success = true;
            for (Attendee a : db_userManagement.getAllAttendees()) {
                a.logout();
                success = success && db_userManagement.logoutUser(a.getID());
            }
            return success;
        }
        finally {
            attendeeLock.unlock();
        }
    }


    /**
     * A function that the communication system can use to check if a login is valid
     * @param userName - the username provided by the request
     * @param password - the password provided by the request
     * @return - A pair consisting of a {@link LoginResponse}, a token, a token, and the number of seconds until the token
     * should expire.
     * If the {@link LoginResponse} is not Valid then the second argument will be null
     */
    @Override
    public Pair<LoginResponse, Pair<String, Long>> login(String userName, String password) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            Pair<LoginResponse, String> response = db_userManagement.checkLogin(userName, password);

            //System.out.println(response.first() + ", " + response.second() + ", " + userName + ", " + password);
            if(response.first() != LoginResponse.Valid){
                return new Pair<>(response.first(), null);
            }
            else{
                return new Pair<>(response.first(), new Pair<>(response.second(), endsAt));
            }
        }
        finally {
            adminLock.unlock();
            attendeeLock.unlock();
        }
    }

    @Override
    public int tokenToID(String token) {//todo check if keeping track of the tokens and ids is more performant
        try {
            adminLock.lock();
            attendeeLock.lock();
            return db_userManagement.tokenToID(token);
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public TokenResponse checkToken(String token) {
        try{
            adminLock.lock();
            if(adminTokens.containsKey(token)){
                return TokenResponse.ValidAdmin;
            }
            else try{
                attendeeLock.lock();
                return db_userManagement.checkToken(token);
            }
            finally {
                attendeeLock.unlock();
            }
        }
        finally {
            adminLock.unlock();
        }
    }

    @Override
    public  String getFreeUserName(String name){
        try{
            adminLock.lock();
            attendeeLock.lock();
            name = name.replaceAll("[^A-Za-z0-9]", ".");
            String nameAux = name;
            int i =1;
            while (db_userManagement.userNameAlreadyUsed(nameAux)){
                nameAux = name + i;
                i++;
            }
            return nameAux;
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }


    /****************** The Voting Management Interface *********/
    @Override
    public Voting getActiveVoting() {
        try {
            votingLock.lock();
            return activeVoting;
        }
        finally {
            votingLock.unlock();
        }
    }

    @Override
    public Voting getVoting( int ID) {
        try{
            votingLock.lock();
            return votings.get(ID);
        }
        finally {
            votingLock.unlock();
        }
    }

    @Override
    public List<Voting> getVotings() {
        try{
            votingLock.lock();
            return new ArrayList<>(votings.values());
        }
        finally {
            votingLock.unlock();
        }
    }

    @Override
    public void addVoting( Voting voting) {
        try{
            votingLock.lock();
            votings.put(voting.getID(), voting);
            voting.register(db_votingManagement);
        }
        finally {
            votingLock.unlock();
        }
    }

    @Override
    public void removeVoting( Voting voting) {
        try{
            votingLock.lock();
            votings.remove(voting.getID(), voting);

        }
        finally {
            votingLock.unlock();
        }
    }

    /****************** The Agenda Management Interface *********/

    @Override
    public Agenda getAgenda() {
        return agenda;
    }


    /****************** The Document Management Interface *********/

    @Override
    public void addDocument( String name, String content) {
        try {
            documentsLock.lock();
            if(documents.containsKey(name)){
                throw new IllegalArgumentException();
            }
            //TODO


        }
        finally {
            documentsLock.unlock();
        }
    }

    @Override
    public void deleteDocument( String name) {
        return;
    }

    @Override
    public void updateDocument( String name, String content) {
        return;
    }

    @Override
    public Document getDocument( String name) {
        return null;
    }

    @Override
    public List<Document> getAllDocuments() {
        return null;
    }

    @Override
    public boolean update(Voting v) {
        try {
            votingLock.lock();
            if (v.getStatus() == VotingStatus.Closed) {
                activeVoting = null;
            }
            if(v.getStatus() == VotingStatus.Running){
                if(activeVoting != null){
                    throw new IllegalStateException("trying to start a vote without closing the previous vote");
                }
                else{
                    activeVoting = v;
                }
            }
            return true;
        }
        finally {
            votingLock.unlock();
        }
    }
}
