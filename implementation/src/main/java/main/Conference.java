package main;

import agenda.Agenda;
import agenda.AgendaManagement;
import agenda.AgendaObserver;
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
import utils.Pair;
import voting.Voting;
import voting.VotingManagement;
import voting.VotingObserver;
import voting.VotingStatus;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Conference implements UserManagement, VotingManagement, RequestManagement, DocumentManagement, AgendaManagement, VotingObserver {

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



    //Creates a clean conference (for debugging)
    public Conference(boolean cleanStart){
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
                true,
                cleanStart
        );


    }

    /**
     * Construct a new or persistent Conference with all the Data below and prepare all DataManagement.
     * @param name the name of the conference
     * @param organizer the organizer of the conference
     * @param startsAt the unix epoch of the time the conference starts at
     * @param endsAt the unix epoch of the time the conference ends at
     * @param admins the admins of the conference
     * @param votings the votings of the conference
     * @param documents the documents of the conference
     * @param documentsPath the storage path for documents
     * @param requests the requests of the conference
     * @param activeVoting the activeVoting ot the conference or null if non-existent
     * @param databasePath the path to the sqlite database
     * @param deguggingInstance if this is a debugging instance
     * @param cleanStart if existing data on the conference should be erased
     */
    public Conference(String name, String organizer, long startsAt, long endsAt, HashMap<Integer,
            Admin> admins, HashMap<Integer,Voting> votings, HashMap<String,Document> documents, String  documentsPath,
                       HashMap<Integer,Request> requests, Voting activeVoting,
                      String databasePath,
                      boolean deguggingInstance, boolean cleanStart) {
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
        if(database.exists() && cleanStart){
            database.delete();
            File[] directoryListing =  new File(documentsPath).listFiles();
            for(int i = 0 ; directoryListing != null && i < directoryListing.length; i++){
                Document d = db_documentManagement.getDocument(directoryListing[i].getName());
                if(d == null){
                    directoryListing[i].delete();
                }
                else{
                    documents.put(d.getName(), d);
                }
            }
        }

        db_votingManagement = new DB_VotingManager(databasePath);

        initUsers();
        initAgenda();
        initDocuments();
        initRequests();
        initVotes();
    }

    /**
     * Initialize Documents for Conference
     */
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

    /**
     * Initialize Users for Conference
     */
    private void initUsers(){
        db_userManagement = new DB_UserManager(databasePath);
        db_userManagement.getAllAdmins().forEach(a -> admins.put(a.getID(), a));
        db_userManagement.getAllAttendees();
    }

    /**
     * Initialize Agenda for Conference
     */
    private void initAgenda(){
        DB_AgendaManager db_agendaManagement = new DB_AgendaManager(databasePath);
        agenda = db_agendaManagement.getAgenda();
        agenda.register(db_agendaManagement);
    }

    /**
     * Initialize Requests for Conference
     */
    private void initRequests(){
        db_requestManagement = new DB_RequestManager(databasePath);
        db_requestManagement.getAllRequests().forEach(r -> requests.put(r.ID, r));
    }

    /**
     * Initialize Votes for Conference
     */
    private void initVotes(){
        //todo
    }


    /****************** The Request Management Interface *********/

    /**
     * Add Request to Request Database
     */
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

    /**
     * Get specific Request from the Database
     * @param ID of the Request
     * @return Request
     */
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

    /**
     * Read all Requests from Database and return them.
     * @return List of Requests
     */
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

    /**
     * Add an Admin with new generated Password and Token to the Database
     * @param a Admin Data
     */
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
            AtomicBoolean alreadyExists = new AtomicBoolean(false);
            db_userManagement.getAllAttendees().forEach(ad -> {
                if(ad.getID() == a.getID()){
                    alreadyExists.set(true);
                }
            });
            if(!alreadyExists.get() && !db_userManagement.addAdmin(a, pwd, gen.generateToken())){
                throw new IllegalArgumentException("Database addition failed");
            }
            admins.put(a.getID(), a);
        }
        finally {
            adminLock.unlock();
        }
    }

    //for debugging
    public void addAttendee(Attendee a, String pwd) {
        assert (debugingInstance); // close the server since this operation is illegal
        try{
            adminLock.lock();
            AtomicBoolean alreadyExists = new AtomicBoolean(false);
            db_userManagement.getAllAttendees().forEach(ad -> {
                if(ad.getID() == a.getID()){
                    alreadyExists.set(true);
                }
            });
            if(!alreadyExists.get() && !db_userManagement.addAttendee(a, pwd, gen.generateToken())){
                throw new IllegalArgumentException("Database addition failed");
            }
        }
        finally {
            adminLock.unlock();
        }
    }

    /**
     * Read all Admins from Database and return them.
     * @return List of Admins
     */
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

    /**
     * Read all personal Data from Admin with AdminId ID and return an Admin Object containing the Data.
     * @param ID AdminId
     * @return Admin
     */
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

    /**
     * Remove Admin with AdminId ID from Database, so he can´t login anymore and delete all Data from the Admin.
     * @param ID AdminId
     */
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

    /**
     * Remove Admin with AdminId ID from Database, so he can´t login anymore.
     * @param ID AdminId
     */
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
            if(!(db_userManagement.logoutUser(ID, gen.generatePassword(), gen.generateToken()))){
                throw new IllegalArgumentException("Admin can not be logged out for unknown reasons");
            }
        }
        finally {
            adminLock.unlock();
        }
    }

    /**
     * Edit Admin with AdminId ID in Database.
     * @param ID AdminId
     * @param a New Admin Data
     */
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

    /**
     * Add new Attendee to the Database.
     * @param a Attendee Data
     */
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

    /**
     * Read all Attendees from Database and return them in a List
     * @return List of Attendees
     */
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

    /**
     * Read specific Attendee Data from Attendee with AttendeeId unserID and return them.
     * @param userID AttendeeId
     * @return Attendee
     */
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

    /**
     * Remove Attendee with AttendeeId userId from Databse, so the Attendee cant login anymore and the Attende Data are deleted.
     * @param userID AttendeeId
     */
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

    /**
     * Remove User with UserId userId from Databse, so the User cant login anymore.
     * @param userID UserId
     */
    @Override
    public void logoutUser(int userID) {
        try{
            attendeeLock.lock();
            if(!db_userManagement.logoutUser(userID, gen.generatePassword(), gen.generateToken())){
                throw new IllegalArgumentException("Attendee can not be logged out for unknown reasons");
            }
        }
        finally {
            attendeeLock.unlock();
        }
    }

    /**
     * Edit existing Attendee in the Databse.
     * @param attendee Attendee
     */
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

    /**
     * Generate a new Password for an User with UserId userId and store it in Database.
     * @param userID UserId
     */
    @Override
    public void generateNewUserPassword(int userID) {
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

    /**
     *  Generate a new Token for an User with UserId userId and store it in Database.
     * @param userID UserId
     */
    @Override
    public void generateNewUserToken(int userID) {
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

    /**
     * Generates for All User new Passwords and store them in Database.
     */
    @Override
    public void generateAllMissingUserPasswords() {
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

    /**
     * Read Password from User with UserId userId and return it.
     * @param userID UserId
     * @return Pair with User and Password
     */
    @Override
    public Pair<User, String> getUserPassword(int userID) {
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

    /**
     * Read Password from  all User and return them.
     * @return List of Pair with User and Password
     */
    @Override
    public List<Pair<User, String>> getAllUsersPasswords() {
        try{
            attendeeLock.lock();
            return db_userManagement.getAllPasswords();
        }
        finally {
            attendeeLock.unlock();
        }
    }

    /**
     * Logout All Attendees from Conference. Invalidate all Token and Password in Database.
     * @return true iff logout was successful
     */
    @Override
    public boolean logoutAllAttendees() {
        try{
            attendeeLock.lock();
            boolean success = true;
            for (Attendee a : db_userManagement.getAllAttendees()) {
                a.logout();
                success = success && db_userManagement.logoutUser(a.getID(), gen.generatePassword(), gen.generateToken());
            }
            return success;
        }
        finally {
            attendeeLock.unlock();
        }
    }

    /**
     * Logout All Attendees which are not Admins from Conference. Invalidate all Token and Password of these.
     * @return true iff logout was successful
     */
    public boolean logoutNonAdmins() {
        try{
            attendeeLock.lock();
            boolean success = true;
            for (Attendee a : db_userManagement.getAllAttendees()) {
                if(isAdmin(a.getID())) continue;
                a.logout();
                success = success && db_userManagement.logoutUser(a.getID(), gen.generatePassword(), gen.generateToken());
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
            db_userManagement.getAllAttendees().forEach(a -> System.out.println(a.getUserName()));
            System.out.println(response.first() + ", " + response.second() + ", " + userName + ", " + password);
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

    /**
     * Read the specific UserID from a User with Token token.
     * @param token Token
     * @return UserId
     */
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

    /**
     * Check if Admin with AdminId id is an Admin.
     * @param id AdminId
     * @return true iff User is an Admin
     */
    @Override
    public boolean isAdmin(int id) {
        return db_userManagement.getAdminData(id) != null;
    }

    /**
     * Checks the Status of the Token token.
     * @param token Token
     * @return TokenResponse
     */
    @Override
    public TokenResponse checkToken(String token) {
        try{
            adminLock.lock();
            attendeeLock.lock();
            if(adminTokens.containsKey(token)){
                System.out.println("Hit 1");
                return TokenResponse.ValidAdmin;
            }
            else {
                TokenResponse res = db_userManagement.checkToken(token);
                System.out.println(res);
                if (res == TokenResponse.ValidAdmin) {
                    adminTokens.put(token, true);
                }
                return res;
            }
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    /**
     * Create an unique UserName from Name name, that isn´t stored in the Database.
     * @param name Name
     * @return Username
     */
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

    /**
     * Get a list of unique groups existing i.e. there is
     * an attendee with the corresponding group in the database
     * @return groups
     */
    public List<String> getExistingGroups() {
        try{
            adminLock.lock();
            attendeeLock.lock();
            List<String> groups = db_userManagement.getAllGroupsFromUser();
            return groups;
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    /**
     * Edit present value of a user.
     * @param username username of the user
     * @param present new present value of the user
     * @return
     */
    public Boolean setPresentValue(String username, Boolean present) {
        try {
            adminLock.lock();
            attendeeLock.lock();
            return db_userManagement.setPresentValueofUser(username, present);
        }
        finally {
            attendeeLock.unlock();
            adminLock.unlock();
        }
    }

    /****************** The Voting Management Interface *********/

    /**
     * Get the Actual Active Voting
     * @return Voting
     */
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

    /**
     * Get created Voting with VotingId ID.
     * @param ID VotingId
     * @return Voting
     */
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

    /**
     * Get all created Votings.
     * @return List of Voting
     */
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

    /**
     * Add finished Voting to the Database
     * @param voting Voting
     */
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

    /**
     * Delete Voting from created Votings
     * @param voting Voting
     */
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

    /**
     * Updated a specific Voting
     * @param v The updates {@link Voting}.
     * @return if the voting was updated successfully
     */
    @Override
    public boolean update(Voting v) {
        try {
            votingLock.lock();
            if (v.getStatus() == VotingStatus.Closed) {
                activeVoting = null;
            }
            if(v.getStatus() == VotingStatus.Running){
                if(activeVoting != null){
                    throw new IllegalArgumentException("trying to start a vote without closing the previous vote");
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

    /**
     * Get the current Agenda.
     * @return Agenda
     */
    @Override
    public Agenda getAgenda() {
        return agenda;
    }

    @Override
    public void updateAgenda(Agenda newAgenda) {
        ConcurrentHashMap<AgendaObserver, Boolean> observers = this.agenda.getObservers();
        for (Map.Entry<AgendaObserver, Boolean> o : observers.entrySet()) {
            newAgenda.register(o.getKey());
        } //Two loops to avoid ConcurrentModification
        for (Map.Entry<AgendaObserver, Boolean> o : observers.entrySet()) {
            newAgenda.unregister(o.getKey());
        }
        this.agenda = newAgenda;

        this.agenda.notifyObservers();
    }


    /****************** The Document Management Interface *********/

    /**
     * Update an existing Document and store the updated Document in the Database.
     * @param name the name of the document to update
     * @param fileType the fileType of the document
     * @param file the file of the document
     * @param isCreation if this is the creation of a document or a new version of an old one
     */
    @Override
    public void updateDocument(String name, String fileType, File file, boolean isCreation) {
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
                    OutputStream os = new FileOutputStream(f, true);
                    f.delete();
                    Files.move(file.toPath(), f.toPath());
                    os.close();
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

    /**
     * Delete an existing Document with DocumentName name in the Database and in the folder.
     * @param name DocumentName
     */
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

    /**
     * Read Document Data with DocumentName name and return it.
     * @param name DocumentName
     * @return Document
     */
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

    /**
     * Read Content from Document with DocumentName name
     * @param name DocumentName
     * @return Byte List
     */
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

    /**
     * Read all Documents and return them.
     * @return List of Documents
     */
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
