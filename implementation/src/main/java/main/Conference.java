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
import user.Admin;
import user.Attendee;
import user.DB_AdminManagement;
import user.DB_AttendeeManagement;
import user.DB_GeneralUserManagement;
import user.LoginResponse;
import user.TokenResponse;
import user.User;
import user.UserManagement;
import utils.Generator;
import utils.Generator_Imp;
import utils.Log;
import utils.Operation;
import utils.OperationResponse;
import utils.Pair;
import voting.DB_VotingManagement;
import voting.Voting;
import voting.VotingManagement;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Conference implements UserManagement, VotingManagement, RequestManagement, DocumentManagement, AgendaManagement {

    //Creates a clean conference (for debugging)
    public Conference(){
        this.name = "Test";
        this.organizer = "Team 23";
        this.startsAt = new Time(System.currentTimeMillis());
        this.endsAt =  this.startsAt = new Time(System.currentTimeMillis() + 1000*60*60);
        this.agenda = new Agenda("");
        this.votings = new HashMap<>();
        this.documents = new HashMap<>();
        this.admins = new HashMap<>();
        this.requests = new HashMap<>();
        this.adminTokens = new HashMap<>();
        this.activeVoting = null;
    }


    public Conference(String name, String organizer, Time startsAt, Time endsAt, Agenda agenda, HashMap<Integer,Voting> votings, HashMap<Integer,Document> documents, Set<String> adminTokens, HashMap<Integer,Request> requests, Voting activeVoting) {
        this.name = name;
        this.organizer = organizer;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.agenda = agenda;
        this.votings = votings;
        this.documents = documents;
        this.requests = requests;
        this.activeVoting = activeVoting;

        adminTokens.forEach(f -> this.adminTokens.put(f, true));
        DB_AgendaManager db_agendaManagement = new DB_AgendaManager("");
        agenda.register(db_agendaManagement);
    }

    //Conference Data
    private String name;

    private String organizer;

    private Time startsAt;
    private Time endsAt;

    private Generator gen = new Generator_Imp();

    private Agenda agenda;
    private HashMap<Integer,Voting> votings;
    private HashMap<Integer,Document> documents;
    private HashMap<Integer,Admin> admins;
    private HashMap<Integer,Request> requests;
    private Voting activeVoting;
    private HashMap<String, Boolean> adminTokens; // a map backed Set

    //Database System //TODO add urls
    private DB_AdminManagement db_adminManagement = new DB_AdminManager("");
    private DB_AttendeeManagement db_attendeeManagement = new DB_AttendeeManager("");
    private DB_DocumentManagement db_documentManagement = new DB_DocumentManager("");
    private DB_GeneralUserManagement db_generalUserManagement = new DB_GeneralUserManager("");
    private DB_RequestManagement db_requestManagement = new DB_RequestManager("");
    private DB_VotingManager db_votingManagement = new DB_VotingManager("");



    //Locks - always take in this order !!!
    private Lock adminLock = new ReentrantLock();
    private Lock attendeeLock = new ReentrantLock();
    private Lock votingLock = new ReentrantLock();
    private Lock requestLock = new ReentrantLock();

    /****************** The Request Management Interface *********/

    @Override
    public OperationResponse addRequest(String token, Request request) {
        return null; // TODO Implement me
    }

    @Override
    public Pair<OperationResponse, Request> getRequest(String adminToken, int ID) {
       return null; //TODO implement me
    }

    @Override
    public Pair<OperationResponse, List<Request>> getAllRequests(String adminToken) {
        OperationResponse response = OperationResponse.AdminSuccess; //TODO check rights
        return new Pair<>( response ,new ArrayList<>(requests.values()));
    }

    /****************** The User Management Interface *********/
    @Override
    public OperationResponse addAdmin(Admin a) {
        try{
            adminLock.lock();
            if(admins.containsKey(a.getID())){
                return OperationResponse.InvalidArguments;
            }
            if(!db_adminManagement.addAdmin(a, gen.generatePassword(), gen.generateToken())){
                return OperationResponse.InvalidArguments;
            }
            admins.put(a.getID(), a);
            return OperationResponse.AdminSuccess;
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
    public OperationResponse removeAdmin(int ID) {
        try {
            adminLock.lock();
            if (admins.get(ID) == null || (!db_generalUserManagement.removeUser(ID))) {
                return OperationResponse.InvalidArguments;
            } else {
                return OperationResponse.AdminSuccess;
            }
        }
        finally {
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse logoutAdmin(int ID) {
        try{
            adminLock.lock();
            if(admins.get(ID) == null){
                return OperationResponse.InvalidArguments;
            }
            else{
                admins.get(ID).logout();
            }
            if(!(db_generalUserManagement.logoutUser(ID))){
                return OperationResponse.InvalidArguments;
            }
            return OperationResponse.AdminSuccess;
        }
        finally {
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse editAdmin(int ID, Admin a) {
        try {
            adminLock.lock();
            if(!db_adminManagement.editAdmin(a)){
                return OperationResponse.InvalidArguments;
            }
            admins.replace(ID, a);
            return OperationResponse.AdminSuccess;
        }
        finally {
            adminLock.unlock();
        }
    }


    @Override
    public OperationResponse addAttendee(String adminToken, Attendee a) {
        try{
            adminLock.lock();
            attendeeLock.lock();

            if(!adminTokens.containsKey(adminToken)){
                return OperationResponse.InvalidToken;
            }

            if(!db_attendeeManagement.addAttendee(a, gen.generatePassword(), gen.generateToken())){
                return OperationResponse.InvalidArguments;
            }

            return OperationResponse.AttendeeSuccess;

        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public Pair<OperationResponse, List<Attendee>> getAllAttendees(String adminToken) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(!adminTokens.containsKey(adminToken)){
                return new Pair<>(OperationResponse.InvalidToken, null);
            }
            else{
                return new Pair<>(OperationResponse.AdminSuccess, db_attendeeManagement.getAllAttendees());
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public Pair<OperationResponse, Attendee> getAttendeeData(String userToken) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(db_generalUserManagement.checkToken(userToken) != TokenResponse.ValidAttendee){
                return new Pair<>(OperationResponse.InvalidToken, null);
            }
            else{
                return new Pair<>(OperationResponse.AttendeeSuccess, db_attendeeManagement.getAttendeeData(userToken));
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse removeAttendee(String adminToken, int userID) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(!adminTokens.containsKey(adminToken)){
                return OperationResponse.InvalidToken;
            }
            else{
                if(!db_generalUserManagement.removeUser(userID)){
                    return OperationResponse.InvalidArguments;
                }
                else{
                    return OperationResponse.AdminSuccess;
                }
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse logoutAttendee(String adminToken, int userID) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(!adminTokens.containsKey(adminToken)){
                return OperationResponse.InvalidToken;
            }
            else{
                if(!db_generalUserManagement.logoutUser(userID)){
                    return OperationResponse.InvalidArguments;
                }
                else{
                    return OperationResponse.AdminSuccess;
                }
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse editAttendee(String adminToken, int userID, Attendee attendee) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(!adminTokens.containsKey(adminToken)){
                return OperationResponse.InvalidToken;
            }
            else{
                if(userID != attendee.getID() || !db_attendeeManagement.editAttendee(attendee)){
                    return OperationResponse.InvalidArguments;
                }
                else{
                    return OperationResponse.AdminSuccess;
                }
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse generateNewAttendeePassword(String adminToken, int userID) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(!adminTokens.containsKey(adminToken)){
                return OperationResponse.InvalidToken;
            }
            else{
                if(!db_generalUserManagement.storeNewPassword(userID, gen.generatePassword())){
                    return OperationResponse.InvalidArguments;
                }
                else{
                    return OperationResponse.AdminSuccess;
                }
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse generateNewAttendeeToken(String adminToken, int userID) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(!adminTokens.containsKey(adminToken)){
                return OperationResponse.InvalidToken;
            }
            else{
                if(!db_generalUserManagement.storeNewToken(userID, gen.generateToken())){
                    return OperationResponse.InvalidArguments;
                }
                else{
                    return OperationResponse.AdminSuccess;
                }
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse generateAllMissingAttendeePasswords(String adminToken) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(!adminTokens.containsKey(adminToken)){
                return OperationResponse.InvalidToken;
            }
            else{
                boolean success = true;
                for (Pair<User, String> p:db_generalUserManagement.getAllPasswords()) {
                    if(p.second() == null){
                        success = success && db_generalUserManagement.storeNewPassword(p.first().getID(), gen.generatePassword());
                    }
                }

               if(success){
                   return OperationResponse.AdminSuccess;
               }
               else{
                    return OperationResponse.InvalidArguments;
               }
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public Pair<OperationResponse, Pair<User, String>> getAttendeePassword(String adminToken, int userID) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(!adminTokens.containsKey(adminToken)){
                return new Pair<>(OperationResponse.InvalidToken, null);
            }
            else{
                for (Pair<User, String> p:db_generalUserManagement.getAllPasswords()) {
                    if(p.first().getID() == userID){
                        return new Pair<>(OperationResponse.AdminSuccess, p);
                    }
                }
                return new Pair<>(OperationResponse.InvalidToken, null);
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public Pair<OperationResponse, List<Pair<User, String>>> getAllAttendeePasswords(String adminToken) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(!adminTokens.containsKey(adminToken)){
                return new Pair<>(OperationResponse.InvalidToken, null);
            }
            else{
                return new Pair<>(OperationResponse.AdminSuccess, db_generalUserManagement.getAllPasswords());
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public boolean logoutAllAttendees() {
        try{
            adminLock.lock();
            attendeeLock.lock();
            boolean success = true;
            for (Attendee a : db_attendeeManagement.getAllAttendees()) {
                a.logout();
                success = success && db_generalUserManagement.logoutUser(a.getID());
            }
            return success;
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse logoutAttendees(String adminToken) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            boolean success = true;
            if(!adminTokens.containsKey(adminToken)){
                return OperationResponse.InvalidToken;
            }
            else{
                for (Attendee a : db_attendeeManagement.getAllAttendees()) {
                    a.logout();
                    success = success && db_generalUserManagement.logoutUser(a.getID());
                }
            }
            if(success){
                return OperationResponse.AdminSuccess;
            }
            else{
                return OperationResponse.InvalidArguments;
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
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
            Pair<LoginResponse, String> response = db_generalUserManagement.checkLogin(name, password);
            if(response.first() != LoginResponse.Valid){
                return new Pair<>(response.first(), null);
            }
            else{
                return new Pair<>(response.first(), new Pair<>(response.second(), endsAt.toInstant().toEpochMilli()));
            }
        }
        finally {
            adminLock.unlock();
            attendeeLock.unlock();
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
            while (db_generalUserManagement.userNameAlreadyUsed(nameAux)){
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
    public Pair<OperationResponse, Voting> getActiveVoting(String token) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(db_generalUserManagement.checkToken(token)==TokenResponse.ValidAdmin){
                return new Pair<>(OperationResponse.AdminSuccess, activeVoting);
            }
            else if(db_generalUserManagement.checkToken(token)==TokenResponse.ValidAttendee){
                return new Pair<>(OperationResponse.AttendeeSuccess, activeVoting);
            }
            else {
                return new Pair<>(OperationResponse.InvalidToken, null);
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public Pair<OperationResponse, Voting> getVoting(String token, int ID) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            votingLock.lock();
            if(votings.get(ID) == null){
                return new Pair<>(OperationResponse.InvalidArguments, null);
            }
            if(db_generalUserManagement.checkToken(token) == TokenResponse.ValidAdmin){
                return new Pair<>(OperationResponse.AdminSuccess, votings.get(ID));
            }
            if(db_generalUserManagement.checkToken(token) == TokenResponse.ValidAttendee){
                return new Pair<>(OperationResponse.AttendeeSuccess, votings.get(ID));
            }
            return new Pair<>(OperationResponse.InvalidToken, null);
        }
        finally {
            votingLock.unlock();
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public Pair<OperationResponse, List<Voting>> getVotings(String token) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            votingLock.lock();
            if(db_generalUserManagement.checkToken(token) == TokenResponse.ValidAdmin){
                return new Pair<>(OperationResponse.AdminSuccess, new ArrayList<>(votings.values()));
            }
            if(db_generalUserManagement.checkToken(token) == TokenResponse.ValidAttendee){
                return new Pair<>(OperationResponse.AttendeeSuccess, new ArrayList<>(votings.values()));
            }
            return new Pair<>(OperationResponse.InvalidToken, null);
        }
        finally {
            votingLock.unlock();
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse addVoting(String token, Voting voting) {
        try{
            adminLock.lock();
            votingLock.lock();

            if(!adminTokens.containsKey(token)){
                return OperationResponse.InvalidToken;
            }
            votings.put(voting.getID(), voting);
            voting.register(db_votingManagement);
            return OperationResponse.AdminSuccess;
        }
        finally {
            votingLock.unlock();
            adminLock.unlock();
        }
    }

    @Override
    public OperationResponse removeVoting(String token, Voting voting) {
        try{
            adminLock.lock();
            votingLock.lock();

            if(!adminTokens.containsKey(token)){
                return OperationResponse.InvalidToken;
            }
            votings.remove(voting.getID(), voting);
            return OperationResponse.AdminSuccess;
        }
        finally {
            votingLock.unlock();
            adminLock.unlock();
        }
    }

    /****************** The Agenda Management Interface *********/

    @Override
    public Pair<OperationResponse, Agenda> getAgenda(String token) {
        try {
            adminLock.lock();
            attendeeLock.lock();
            TokenResponse tokentype = db_generalUserManagement.checkToken(token);
            if(tokentype == TokenResponse.ValidAdmin){
                return new Pair<>(OperationResponse.AdminSuccess, agenda);
            }
            else if(tokentype == TokenResponse.ValidAttendee){
                return new Pair<>(OperationResponse.AttendeeSuccess, agenda);
            }
            else{
                return new Pair<>(OperationResponse.InvalidToken, null);
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }


    /****************** The Document Management Interface *********/

    @Override
    public OperationResponse addDocument(String adminToken, String name, String content) {
        return null;
    }

    @Override
    public OperationResponse deleteDocument(String adminToken, String name) {
        return null;
    }

    @Override
    public OperationResponse updateDocument(String adminToken, String name, String content) {
        return null;
    }

    @Override
    public Pair<OperationResponse, Document> getDocument(String token, String name) {
        return null;
    }

    @Override
    public Pair<OperationResponse, List<Document>> getAllDocuments(String token) {
        return null;
    }
}
