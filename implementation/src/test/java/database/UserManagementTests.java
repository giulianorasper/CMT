package database;

import org.junit.*;
import user.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserManagementTests extends DatabaseTests {

    @Test
    public void validAttendeeCredentials(){
        Attendee max = new Attendee("Max Mustermann", "email@email.muster", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        assertTrue("Attendee couldn't be added", dbGen.addAttendee(max, "1234", "42"));
        assertEquals(TokenResponse.ValidAttendee, dbGen.checkToken("42"));
    }

    @Test
    public void validAdminCredentials(){
        Admin stephan = new Admin("Stephan Mustermann", "email@email.muster", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        assertTrue("Admin couldn't be added", dbGen.addAdmin(stephan, "rue1831978", "token"));
        assertEquals(LoginResponse.Valid, dbGen.checkLogin("AlmightyStephan", "rue1831978").first());
        assertEquals(TokenResponse.ValidAdmin, dbGen.checkToken("token"));
    }

}
