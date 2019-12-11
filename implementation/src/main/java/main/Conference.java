package main;

import agenda.Agenda;
import agenda.AgendaManagement;
import agenda.DB_AgendaManagement;
import agenda.Topic;
import com.google.gson.annotations.Expose;
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

import javax.print.Doc;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.*;
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
                new HashMap<Integer, Admin>(),
                new HashMap<Integer, Voting>(),
                new HashMap<String, Document>(),
                "./docs",
                new HashMap<Integer, Request>(),
                null,
                "./testdb/testdb.db",
                true
        );

    }


    public Conference(String name, String organizer, long startsAt, long endsAt, HashMap<Integer,
            Admin> admins, HashMap<Integer,Voting> votings, HashMap<String,Document> documents, String  documentsPath,
                       HashMap<Integer,Request> requests, Voting activeVoting,
                      String databasePath,
                      boolean deguggingInstance) {
        this.name = name;
        this.organizer = organizer;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.votings = votings;
        this.documents = documents;
        this.requests = requests;
        this.activeVoting = activeVoting;
        this.documentsPath = documentsPath;
        this.databasePath = databasePath;

        this.debugingInstance  = deguggingInstance;
        this.admins = admins;

        this.adminTokens = new HashMap<>();


        File database = new File(databasePath);
        if(database.exists()){
            if(deguggingInstance){
            //    database.delete();
            }
        }


        db_votingManagement = new DB_VotingManager(databasePath);

        initUsers();
        initAgenda();
        initDocuments();
        initRequests();
        initVotes();
    }

    private void initDocuments(){
        db_documentManagement = new DB_DocumentManager(databasePath);
        File documentsFolder = new File(documentsPath);

        if(!documentsFolder.exists() && !documentsFolder.mkdir()){
            throw new IllegalArgumentException("Could not create directory " + documentsPath);
        }
        if(documentsFolder.exists() && !documentsFolder.isDirectory()){
            throw new IllegalArgumentException("Could not create directory " + documentsPath +" , because a file with that name already exists");
        }
        if(documentsFolder.exists() && documentsFolder.isDirectory()){
            File[] directoryListing = documentsFolder.listFiles();
            for(int i = 0 ; i < directoryListing.length; i++){
                Document d = db_documentManagement.getDocument(directoryListing[i].getName());
                if(d == null){
                    directoryListing[i].delete();
                }
                else{
                    documents.put(d.getName(), d);
                }
            }
        }
    }

    private void initUsers(){
        db_userManagement = new DB_UserManager(databasePath);
        db_userManagement.getAllAdmins().forEach(a -> admins.put(a.getID(), a));
        db_userManagement.getAllAttendees();
    }

    private void initAgenda(){
        DB_AgendaManager db_agendaManagement = new DB_AgendaManager(databasePath);
        agenda = db_agendaManagement.getAgenda();
        System.out.println(agenda);
        agenda.register(db_agendaManagement);
    }

    private void initRequests(){
        db_requestManagement = new DB_RequestManager(databasePath);
        db_requestManagement.getAllRequests().forEach(r -> requests.put(r.ID, r));
    }

    private void initVotes(){
        //todo
    }

    //Conference Data
    @Expose
    private String name;
    @Expose
    private String organizer;
    @Expose
    private long startsAt;
    @Expose
    private long endsAt;

    private boolean debugingInstance;

    private Generator gen = new Generator_Imp();

    private String  documentsPath ;
    private String databasePath;

    private Agenda agenda;
    private HashMap<Integer,Voting> votings;
    private HashMap<String, Document> documents;
    private HashMap<Integer,Admin> admins;
    private HashMap<Integer,Request> requests;
    private Voting activeVoting;
    private HashMap<String, Boolean> adminTokens; // a map backed Set
    private HashMap<String, Boolean> volatileUserNames = new HashMap<>(); // user names that are reserved even though the corresponding user is not added (jet)

    //Database System
    private DB_DocumentManagement db_documentManagement;
    private DB_UserManagement db_userManagement;
    private DB_RequestManagement db_requestManagement;
    private DB_VotingManager db_votingManagement;



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
            attendeeLock.lock();
            volatileUserNames.remove(a.getUserName());
            if(!db_userManagement.addAdmin(a, gen.generatePassword(), gen.generateToken())){
                throw new IllegalArgumentException("Database addition failed");
            }
            admins.put(a.getID(), a);
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }


    //for debugging
    public void addAdmin(Admin a, String pwd) {
        assert (debugingInstance); // close the server since this operation is illegal
        try{
            adminLock.lock();
            if(!db_userManagement.addAdmin(a, pwd, gen.generateToken())){
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
            adminLock.lock();
            attendeeLock.lock();
            volatileUserNames.remove(a.getUserName());
            if(!db_userManagement.addAttendee(a, gen.generatePassword(), gen.generateToken())){
                throw new IllegalArgumentException("Attendee can not be edited for unknown reasons");
            }

        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
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
    public void generateAllMissingAttendeePasswords() {
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
    public boolean isAdmin(int id) {
        return false;
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
            while (volatileUserNames.containsKey(nameAux) || db_userManagement.userNameAlreadyUsed(nameAux)){
                nameAux = name + i;
                i++;
            }
            volatileUserNames.put(nameAux, true);
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

    /****************** The Agenda Management Interface *********/

    @Override
    public Agenda getAgenda() {
        return agenda;
    }


    /****************** The Document Management Interface *********/

    @Override
    public void updateDocument(String name, String fileType, byte[] fileBytes, boolean isCreation) {
        try {
            documentsLock.lock();
            String fullName = name;
            System.out.println(fullName);
            File f;
            if(!documents.containsKey(fullName)) {
                f = new File(documentsPath + "/" + fullName);
            }
            else{
                f = documents.get(fullName).getFile();
            }
            if(f.exists() && isCreation){
                throw new IllegalArgumentException("File already exists");
            }
            if(!f.exists() && !isCreation){
                throw new IllegalArgumentException("File does not exist");
            }
            try {
                if(f.exists() ||  f.createNewFile()){
                    Files.write(f.toPath(), fileBytes);
                }
                else{
                    throw new IllegalArgumentException();
                }

            }
            catch (IOException e){
                throw new IllegalArgumentException(e.getMessage());
            }
            if(isCreation){
                Document doc = new Document(f.getPath(), fullName);
                if(db_documentManagement.addDocument(doc)) {
                    documents.put(fullName, doc);
                }
            }
            else{
                documents.get(fullName).incrementRevision();
            }

        }
        finally {
            documentsLock.unlock();
        }
    }

    @Override
    public void deleteDocument( String name) {
        try{
            documentsLock.lock();
            if(!documents.containsKey(name)){
                throw new IllegalArgumentException("Document does not exist");
            }
            File f = documents.get(name).getFile();
            if(!f.delete()){
                throw new IllegalArgumentException("Could not remove document from server");
            }
            if(!db_documentManagement.deleteDocument(name)){
                throw new IllegalArgumentException("Could not remove the document from the database");
            }
            documents.remove(name);

        }
        finally {
            documentsLock.unlock();
        }
    }

    @Override
    public Document getDocument(String name){
        try{
            documentsLock.lock();
            if(!documents.containsKey(name)){
                throw new IllegalArgumentException("Document does not exist");
            }
            return documents.get(name);
        }
        finally {
            documentsLock.unlock();
        }
    }


    @Override
    public byte[] getDocumentContent( String name) {
        try{
            documentsLock.lock();
            if(!documents.containsKey(name)){
                throw new IllegalArgumentException("file does not exist");
            }

            File f = documents.get(name).getFile();

            byte[] fileBytes = new byte[(int)f.length()];
            try{
                FileInputStream fis = new FileInputStream(f);
                fis.read(fileBytes);
                fis.close();
                return fileBytes;

            }
            catch (IOException e){
                throw new IllegalArgumentException("Could not read file");
            }
        }
        finally {
            documentsLock.unlock();
        }
    }

    @Override
    public List<Document> getAllDocuments() {
        try {
            documentsLock.lock();
            return new ArrayList<>(documents.values());
        }
        finally {
            documentsLock.unlock();
        }
    }



}
