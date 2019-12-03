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
import utils.OperationResponse;
import utils.Pair;
import voting.DB_VotingManagement;
import voting.Voting;
import voting.VotingManagement;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
        this.votings = new ConcurrentHashMap<>();
        this.documents = new ConcurrentHashMap<>();
        this.admins = new ConcurrentHashMap<>();
        this.requests = new ConcurrentHashMap<>();
        this.adminTokens = new ConcurrentHashMap<>();
        this.activeVoting = null;
    }


    public Conference(String name, String organizer, Time startsAt, Time endsAt, Agenda agenda, ConcurrentHashMap<Integer,Voting> votings, ConcurrentHashMap<Integer,Document> documents, Set<String> adminTokens, ConcurrentHashMap<Integer,Request> requests, Voting activeVoting) {
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
    }

    //Conference Data
    private String name;

    private String organizer;

    private Time startsAt;
    private Time endsAt;

    private Agenda agenda;
    private ConcurrentHashMap<Integer,Voting> votings;
    private ConcurrentHashMap<Integer,Document> documents;
    private ConcurrentHashMap<Integer,Admin> admins;
    private ConcurrentHashMap<Integer,Request> requests;
    private Voting activeVoting;
    private ConcurrentHashMap<String, Boolean> adminTokens; // a map backed Set

    //Database System //TODO add urls
    private DB_AdminManagement db_adminManagement = new DB_AdminManager("");
    private DB_AgendaManagement db_agendaManagement = new DB_AgendaManager("");
    private DB_AttendeeManagement db_attendeeManagement = new DB_AttendeeManager("");
    private DB_DocumentManagement db_docuemntManagement = new DB_DocumentManager("");
    private DB_GeneralUserManagement db_generalUserManagement = new DB_GeneralUserManager("");
    private DB_RequestManagement db_requestManagement = new DB_RequestManager("");
    private DB_VotingManagement db_votingManagement = new DB_VotingManager("");



    //Locks - always take in this order !!!
    private Lock adminLock = new ReentrantLock();
    private Lock attendeeLock = new ReentrantLock();
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
            admins.put(a.getID(), a);
            return OperationResponse.AdminSuccess;
        }
        finally {
            adminLock.unlock();
        }
    }

    @Override
    public List<Admin> getAllAdmins() {
        return new ArrayList<>(admins.values());
    }

    @Override
    public Admin getAdminPersonalData(int ID) {
        return admins.get(ID);
    }

    @Override
    public OperationResponse removeAdmin(int ID) {
        if(admins.remove(ID) == null){
            return OperationResponse.InvalidArguments;
        }
        else{
            return OperationResponse.AdminSuccess;
        }
    }

    @Override
    public OperationResponse logoutAdmin(int ID) {
        return null; //TODO implement me
    }

    @Override
    public OperationResponse editAdmin(int ID, Admin a) {
        return null; //TODO implement me
    }

    @Override
    public OperationResponse generateNewPassword(int ID) {
        return null; //TODO implement me
    }

    @Override
    public OperationResponse generateNewToken(int ID) {
        return null;//TODO implement me
    }

    @Override
    public OperationResponse addAttendee(String adminToken, Attendee a) {
        return null;
    }

    @Override
    public Pair<OperationResponse, List<Attendee>> getAllAttendees(String adminToken) {
        //TODO check rights
        return null;
    }

    @Override
    public Pair<OperationResponse, Attendee> getAttendeeData(String userToken) {
        //TODO implement me
        return null;
    }

    @Override
    public OperationResponse removeAttendee(String adminToken, int userID) {
        return null;//TODO implement me
    }

    @Override
    public OperationResponse logoutAttendee(String adminToken, int userID) {
        return null;//TODO implement me
    }

    @Override
    public OperationResponse editAttendee(String adminToken, int userID) {
        return null;//TODO implement me
    }

    @Override
    public OperationResponse generateNewAttendeePassword(String adminToken, int userID) {
        return null;//TODO implement me
    }

    @Override
    public OperationResponse generateNewAttendeeToken(String adminToken, int userID) {
        return null;//TODO implement me
    }

    @Override
    public OperationResponse generateAllMissingAttendeePasswords(String adminToken) {
        return null;//TODO implement me
    }

    @Override
    public Pair<OperationResponse, Pair<User, String>> getAttendeePassword(String adminToken, int userID) {
        return null;//TODO implement me
    }

    @Override
    public Pair<OperationResponse, List<Pair<User, String>>> getAllAttendeePasswords(String adminToken) {
        return null;//TODO implement me
    }

    @Override
    public boolean logoutAllAttendees() {
        return false;//TODO implement me
    }

    @Override
    public OperationResponse logoutAttendees(String adminToken) {
        return null;//TODO implement me
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
        return null;
    }


    /****************** The Voting Management Interface *********/
    @Override
    public Pair<OperationResponse, Voting> getActiveVoting(String token) {
        return null;//TODO implement me
    }

    @Override
    public Pair<OperationResponse, Voting> getVoting(String token, int ID) {
        return null;//TODO implement me
    }

    @Override
    public Pair<OperationResponse, Voting> getVotings(String token) {
        return null;//TODO implement me
    }

    @Override
    public OperationResponse addVoting(String token, Voting voting) {
        return null;//TODO implement me
    }

    @Override
    public OperationResponse removeVoting(String token, Voting voting) {
        return null;//TODO implement me
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
