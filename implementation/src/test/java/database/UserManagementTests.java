package database;

import org.junit.*;
import user.*;
import utils.Pair;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserManagementTests extends DatabaseTests {

    @Test
    public void validAttendeeCredentials(){
        Attendee max = new Attendee("Max Mustermann", "email@email.muster", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        assertTrue("Attendee couldn't be added", dbGen.addAttendee(max, "1234", "42"));
        assertEquals(TokenResponse.ValidAttendee, dbGen.checkToken("42"));
        assertEquals(dbGen.getAttendeeData(max.getID()).getName(), max.getName());
        assertEquals(dbGen.getAttendeeData(max.getID()).getEmail(), max.getEmail());
        assertEquals(dbGen.getAttendeeData(max.getID()).getUserName(), max.getUserName());
        assertEquals(dbGen.getAttendeeData(max.getID()).getGroup(), max.getGroup());
        assertEquals(dbGen.getAttendeeData(max.getID()).getResidence(), max.getResidence());
        assertEquals(dbGen.getAttendeeData(max.getID()).getFunction(), max.getFunction());
        assertEquals(dbGen.getAttendeeData(max.getID()).getID(), max.getID());



    }

    @Test
    public void validAdminCredentials(){
        Admin stephan = new Admin("Stephan Mustermann", "email@email.muster", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        assertTrue("Admin couldn't be added", dbGen.addAdmin(stephan, "rue1831978", "token"));
        assertEquals(LoginResponse.Valid, dbGen.checkLogin("AlmightyStephan", "rue1831978").first());
        assertEquals(TokenResponse.ValidAdmin, dbGen.checkToken("token"));


        assertEquals(dbGen.getAdminData(stephan.getID()).getName(), stephan.getName());
        assertEquals(dbGen.getAdminData(stephan.getID()).getEmail(), stephan.getEmail());
        assertEquals(dbGen.getAdminData(stephan.getID()).getUserName(), stephan.getUserName());
        assertEquals(dbGen.getAdminData(stephan.getID()).getGroup(), stephan.getGroup());
        assertEquals(dbGen.getAdminData(stephan.getID()).getFunction(), stephan.getFunction());
        assertEquals(dbGen.getAdminData(stephan.getID()).getResidence(), stephan.getResidence());
        assertEquals(dbGen.getAdminData(stephan.getID()).getID(), stephan.getID());
    }

    @Test
    public void removeUser(){
        Attendee max = new Attendee("Max Mustermann", "email@email.muster", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        Admin stephan = new Admin("Stephan Mustermann", "enmail@email.muster", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        dbGen.addAttendee(max, "1234", "42");
        dbGen.addAdmin(stephan, "1111", "9999");
        List<Integer> ids = dbGen.getIDs();
        assertTrue(ids.get(0) == max.getID());
        assertTrue(ids.get(1) == stephan.getID());
        int id = dbGen.tokenToID("42");
        assertTrue(dbGen.removeUser(id));
        int id1 = dbGen.tokenToID("9999");
        assertTrue(dbGen.removeUser(id1));
    }

    @Test
    public void logoutUser(){
        Attendee max = new Attendee("Max Mustermann", "email@email.muster", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        Admin stephan = new Admin("Stephan Mustermann", "enmail@email.muster", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        dbGen.addAttendee(max, "1234", "42");
        dbGen.addAdmin(stephan, "1111", "9999");
        int id = dbGen.tokenToID("42");
        assertTrue(dbGen.logoutUser(id));
        int id1 = dbGen.tokenToID("9999");
        assertTrue(dbGen.logoutUser(id1));
    }

    @Test
    public void getCorrectPasswords(){
        Attendee max = new Attendee("Max Mustermann", "email@email.muster", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        Admin stephan = new Admin("Stephan Mustermann", "enmail@email.muster", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        dbGen.addAttendee(max, "1234", "42");
        dbGen.addAdmin(stephan, "1111", "9999");
        List<Pair<User, String>> pw = dbGen.getAllPasswords();
        assertEquals(pw.get(0).second(), "1234");
        assertEquals(pw.get(0).first().getID(),max.getID());
        assertEquals(pw.get(1).second(), "1111");
        assertEquals(pw.get(1).first().getID(), stephan.getID());
    }

    @Test
    public void newTokenToUser(){
        Attendee max = new Attendee("Max Mustermann", "email@email.muster", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        Admin stephan = new Admin("Stephan Mustermann", "enmail@email.muster", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        dbGen.addAttendee(max, "1234", "42");
        dbGen.addAdmin(stephan, "1111", "9999");

        assertTrue(dbGen.storeNewToken(0, "11"));
        assertTrue(dbGen.storeNewToken(1, "22"));

        assertEquals(dbGen.tokenToID("11"), 0);
        assertEquals(dbGen.tokenToID("22"), 1);
        try {
            int i =dbGen.tokenToID("42");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void newPasswordToUser(){
        Attendee max = new Attendee("Max Mustermann", "email@email.muster", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        Admin stephan = new Admin("Stephan Mustermann", "enmail@email.muster", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        dbGen.addAttendee(max, "1234", "42");
        dbGen.addAdmin(stephan, "1111", "9999");

        assertTrue(dbGen.storeNewPassword(0, "121"));
        assertTrue(dbGen.storeNewPassword(1, "2222"));

        assertEquals(dbGen.getAllPasswords().get(0).second(), "121");
        assertEquals(dbGen.getAllPasswords().get(1).second(),"2222");
    }

    @Test
    public void existUserName(){
        Attendee max = new Attendee("Max Mustermann", "email@email.muster", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        Admin stephan = new Admin("Stephan Mustermann", "enmail@email.muster", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        dbGen.addAttendee(max, "1234", "42");
        dbGen.addAdmin(stephan, "1111", "9999");
        String a = dbGen.getAttendeeData(0).getUserName();
        assertTrue(dbGen.userNameAlreadyUsed(max.getUserName()));
        assertTrue(dbGen.userNameAlreadyUsed(stephan.getUserName()));
        assertTrue(!(dbGen.userNameAlreadyUsed("username")));
    }

    @Test
    public void severalAttendeesAndAdmins(){
        Attendee max = new Attendee("Max Mustermann", "email@email.muster", "Max.Mustermann", "RCDS", "Differten", "Straßenkehrer", 0);
        Attendee stephan = new Attendee("Stephan Mustermann", "enmail@email.muster", "AlmightyStephan", "project23", "Winterwunderland", "group member", 1);
        DB_UserManagement dbGen = this.getGeneralUserDB();

        dbGen.addAttendee(max, "1234", "42");
        dbGen.addAttendee(stephan, "1111", "9999");
        List<Attendee> attendees = dbGen.getAllAttendees();
        assertEquals(attendees.get(0).getID(),max.getID());
        assertEquals(attendees.get(1).getID(), stephan.getID());

        Admin Alex = new Admin("Alex Mustermann", "almail@email.muster", "Alex.Mustermann", "RCDS", "Diffeasarten", "Straßenkehrer", 2);
        Admin Kamran = new Admin("Kamran Mustermann", "kamnmail@email.muster", "Kamran", "project23", "Winterwdfgunderland", "group member", 3);
        dbGen.addAdmin(Alex, "345", "3456");
        dbGen.addAdmin(Kamran, "2345", "3245");
        List<Admin> admins = dbGen.getAllAdmins();
        assertEquals(admins.get(0).getID(), Alex.getID());
        assertEquals(admins.get(1).getID(), Kamran.getID());
    }

}
