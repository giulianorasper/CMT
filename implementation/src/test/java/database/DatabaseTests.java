package database;

import agenda.DB_AgendaManagement;
import document.DB_DocumentManagement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request.DB_RequestManagement;
import user.DB_AdminManagement;
import user.DB_AttendeeManagement;
import user.DB_GeneralUserManagement;
import user.GeneralUserManagement;
import voting.DB_VotingManagement;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

public abstract class DatabaseTests {

    private URI path;

    private DB_GeneralUserManagement generalUserDB;
    private DB_AttendeeManagement attendeeDB;
    private DB_AdminManagement adminDB;
    private DB_AgendaManagement agendaDB;
    private DB_DocumentManagement documentDB;
    private DB_RequestManagement requestDB;
    private DB_VotingManagement votingDB;

    @Before
    public void init() throws IOException {
        path = Paths.get("test_db/database.db").toUri();
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();

        this.generalUserDB = new DB_GeneralUserManager(this.path.toString());
        this.adminDB = new DB_AdminManager(this.path.toString());
        this.attendeeDB = new DB_AttendeeManager(this.path.toString());
        this.agendaDB = new DB_AgendaManager(this.path.toString());
        this.documentDB = new DB_DocumentManager(this.path.toString());
        this.requestDB = new DB_RequestManager(this.path.toString());
        this.votingDB = new DB_VotingManager(this.path.toString());
    }

    @After
    public void cleanup() {
        new File(path).delete();
    }

    protected DB_GeneralUserManagement getGeneralUserDB(){
        return this.generalUserDB;
    }

    protected DB_AttendeeManagement getAttendeeDB(){
        return this.attendeeDB;
    }

    protected DB_AdminManagement getAdminDB(){
        return this.adminDB;
    }

    protected DB_AgendaManagement getAgendaDB(){
        return this.agendaDB;
    }

    protected DB_DocumentManagement getDocumentDB(){
        return this.documentDB;
    }

    protected DB_RequestManagement getRequestDB(){
        return this.requestDB;
    }

    protected DB_VotingManagement getVotingDB(){
        return this.votingDB;
    }

}
