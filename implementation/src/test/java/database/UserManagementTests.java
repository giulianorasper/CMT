package database;

import agenda.DB_AgendaManagement;
import org.junit.*;
import user.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserManagementTests extends DatabaseTests {

    @Test
    public void validAttendeeCredentials(){
        Attendee max = new Attendee("Max Mustermann", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        DB_AttendeeManagement dbAtt = this.getAttendeeDB();
        DB_GeneralUserManagement dbGen = this.getGeneralUserDB();

        assertTrue("Attendee couldn't be added", dbAtt.addAttendee(max, "1234", "42"));
        assertEquals(TokenResponse.ValidAttendee, dbGen.checkToken("42"));
    }

    @Test
    public void validAdminCredentials(){
        Admin stephan = new Admin("Stephan Mustermann", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_AdminManagement dbAdmin = this.getAdminDB();
        DB_GeneralUserManagement dbGen = this.getGeneralUserDB();

        assertTrue("Admin couldn't be added", dbAdmin.addAdmin(stephan, "rue1831978", "token"));
        assertEquals(LoginResponse.Valid, dbGen.checkLogin("AlmightyStephan", "rue1831978").first());
        assertEquals(TokenResponse.ValidAdmin, dbGen.checkToken("token"));
    }

}
