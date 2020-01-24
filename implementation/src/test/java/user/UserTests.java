package user;

import main.Conference;
import org.junit.Before;
import org.junit.Test;
import utils.Pair;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserTests {
    Conference conf;
    @Before
    public void createConference(){

        conf = new Conference(true);
    }

    /**
     * Tests what happens if multiple admins add users with the same email
     */
    @Test
    public void addUserConcurrentlySameEmail(){
        int threadCount = 100;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger doneCount = new AtomicInteger(0);
        AtomicBoolean go = new AtomicBoolean(false);

        for(int i = 0; i < threadCount; i ++){
            (new Thread(){
                @Override
                public void run(){
                    Attendee a = new Attendee("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
                    while (!go.compareAndSet(true, true)){/*wait*/}
                    boolean successfull;
                    try {
                        conf.addAttendee(a);
                        successfull = true;
                    }
                    catch (IllegalArgumentException e){
                        successfull = false;
                    }
                    int res = successCount.get();
                    while (successfull && !successCount.compareAndSet(res, res+1)){
                        res = successCount.get();
                    }
                    res = doneCount.get();
                    while (!doneCount.compareAndSet(res, res+1)){
                        res = doneCount.get();
                    }
                }
            }).start();
        }
        go.compareAndSet(false, true);
        int res = doneCount.get();
        while (res != threadCount){
            res = doneCount.get();
        }
        assertEquals("Expected a single addition to be successfull, but there were "+ successCount.get(), 1, successCount.get());
    }


    /**
     * Tests what happens if multiple admins add users concurrently
     */
    @Test
    public void addUserConcurrentlyDifferentEmail(){
        int threadCount = 10;
        AtomicInteger doneCount = new AtomicInteger(0);
        AtomicBoolean go = new AtomicBoolean(false);

        for(int i = 0; i < threadCount; i ++){
            AtomicInteger aux = new AtomicInteger(i);
            (new Thread(){
                @Override
                public void run(){
                    Attendee a = new Attendee("Mike", "Mike@Gebirge"+aux.get()+".tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
                    while (!go.compareAndSet(true, true)){/*wait*/}
                    try {
                        conf.addAttendee(a);
                    }
                    catch (IllegalArgumentException e){
                        fail("Failed to add an attendee " + a);
                    }
                   int res;
                    res = doneCount.get();
                    while (!doneCount.compareAndSet(res, res+1)){
                        res = doneCount.get();
                    }
                }
            }).start();
        }
        go.compareAndSet(false, true);
        int res = doneCount.get();
        while (res != threadCount){
            res = doneCount.get();
        }
    }

    /**
     * Tests what happens if multiple admins edit different users concurrently
     */
    @Test
    public void editUsersConcurrently(){
        int threadCount = 10;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger doneCount = new AtomicInteger(0);
        AtomicBoolean go = new AtomicBoolean(false);

        int[] attendeeIds = new int[threadCount];

        for(int  i = 0; i < threadCount; i++){
            Attendee a = new Attendee("Mike", "Miketest@Gebirge"+i+".tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
            conf.addAttendee(a);
            attendeeIds[i] = a.getID();
        }


        for(int i = 0; i < threadCount; i ++){
            AtomicInteger aux = new AtomicInteger(i);
            (new Thread(){
                @Override
                public void run(){
                    while (!go.compareAndSet(true, true)){/*wait*/}
                    boolean successfull;
                    Attendee a = new Attendee("Mike"+ aux.get(), "Mike@Gebirge"+aux.get()+".tods", conf.getFreeUserName("Mike"), "RCDS", "MPI" + aux.get(), "SysAdmin", attendeeIds[aux.get()]);

                    try {
                        conf.editAttendee(a);

                    }
                    catch (IllegalArgumentException e){
                        fail("Failed to edit attendee " + a);
                    }

                    int res;
                    res = doneCount.get();
                    while (!doneCount.compareAndSet(res, res+1)){
                        res = doneCount.get();
                    }

                    while (true){
                        if(doneCount.get() == threadCount){
                            break;
                        }
                    }

                    assertEquals("Attendee has different name despite successful edit " + a,  "Mike" + aux.get(), conf.getAttendeeData(a.getID()).name);
                    assertEquals("Attendee has different residence despite successful edit " + a, "MPI" + aux.get(), conf.getAttendeeData(a.getID()).residence);



                }
            }).start();
        }
        go.compareAndSet(false, true);
        int res = doneCount.get();
        while (res != threadCount){
            res = doneCount.get();
        }

}

    /**
     * Tests what happens if multiple login requests come in, some successful and some unsuccessful
     */
    @Test
    public void logAttendeesInAndOut(){
        int threadCount = 50;
        AtomicInteger doneCount = new AtomicInteger(0);
        AtomicBoolean go = new AtomicBoolean(false);

        int[] attendeeIds = new int[threadCount];
        String[] attendeeNames = new String[threadCount];

        for(int  i = 0; i < threadCount; i++){
            Attendee a = new Attendee("Mike", "Mike2@Gebirge"+i+".tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
            conf.addAttendee(a);
            attendeeIds[i] = a.getID();
            attendeeNames[i] = a.getUserName();
            conf.generateAllMissingUserPasswords();
        }


        for(int i = 0; i < threadCount; i ++){
            AtomicInteger aux = new AtomicInteger(i);
            (new Thread(){
                @Override
                public void run(){
                    while (!go.compareAndSet(true, true)){/*wait*/}


                        String password = conf.getUserPassword(attendeeIds[aux.get()]).second();
                        Pair<LoginResponse, Pair<String, Long>> response;
                        if (aux.get() % 2 == 1){
                            response = conf.login(attendeeNames[aux.get()], password);
                            if(response.first() != LoginResponse.Valid){
                                fail("Failed to perform a valid login");
                            }
                            conf.setPresentValue(attendeeNames[aux.get()],true);
                            assertEquals("Got a wrong id for attendee ", attendeeIds[aux.get()], conf.tokenToID(response.second().first()));
                        }
                        else {
                            response = conf.login("Mike" + aux.get(), password + "a");
                            if(response.first() == LoginResponse.Valid){
                                fail("Managed to login with an invalid password");
                            }
                        }




                    int res;
                    res = doneCount.get();
                    while (!doneCount.compareAndSet(res, res+1)){
                        res = doneCount.get();
                    }

                    while (true){
                        if(doneCount.get() == threadCount){
                            System.out.println(doneCount.get());
                            break;
                        }
                    }
                }
            }).start();
        }


        go.compareAndSet(false, true);
        int res = doneCount.get();
        while (res != threadCount){
            res = doneCount.get();
        }

        int loginCount = (int) conf.getAllAttendees().stream().filter(Attendee::isPresent).count();

        assertEquals("Expected half of the attendees to be logged in", threadCount/2 , loginCount);

        conf.logoutNonAdmins(false);
        loginCount = (int) conf.getAllAttendees().stream().filter(Attendee::isPresent).count();
        assertEquals("Expected aa attendees to be logged out", 0 , loginCount);

    }

    @Test
    public void invalidLogin(){
        Attendee a = new Attendee("Mike", "Mikes@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAttendee(a);
        String password = conf.getUserPassword(a.getID()).second();
        conf.logoutUser(a.getID());
        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() == LoginResponse.Valid){
            fail("Managed to log in a user which should not be loged in");
        }

    }

    @Test
    public void invalidLogin2(){
        Attendee a = new Attendee("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAttendee(a);
        String password = conf.getUserPassword(a.getID()).second();
        conf.logoutAllUsers();
        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() == LoginResponse.Valid){
            fail("Managed to log in a user which should not be loged in");
        }

    }

    @Test
    public void invalidLogin3(){
        Attendee a = new Attendee("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAttendee(a);
        String password = conf.getUserPassword(a.getID()).second();
        conf.logoutNonAdmins(false);
        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() == LoginResponse.Valid){
            fail("Managed to log in a user which should not be loged in");
        }

    }

    @Test
    public void invalidLogin4(){
        Attendee a = new Attendee("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAttendee(a);
        String password = conf.getUserPassword(a.getID()).second();
        conf.removeAttendee(a.getID());
        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() == LoginResponse.Valid){
            fail("Managed to log in a user which should not be loged in");
        }

    }

    @Test
    public void invalidLogin5(){
        Admin a = new Admin("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAdmin(a);
        String password = conf.getUserPassword(a.getID()).second();
        conf.removeAdmin(a.getID());
        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() == LoginResponse.Valid){
            fail("Managed to log in a user which should not be loged in");
        }

    }

    @Test
    public void invalidLogin6(){
        Admin a = new Admin("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAdmin(a);
        String password = conf.getUserPassword(a.getID()).second();
        conf.logoutAdmin(a.getID());
        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() == LoginResponse.Valid){
            fail("Managed to log in a user which should not be loged in");
        }

    }


    @Test
    public void invalidLogin7(){
        Attendee a = new Attendee("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAttendee(a);
        String password = conf.getUserPassword(a.getID()).second();
        conf.generateNewUserPassword(a.getID());
        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() == LoginResponse.Valid){
            fail("Managed to log in a user which should not be loged in");
        }

    }


    @Test
    public void validLogin(){
        Admin a = new Admin("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAdmin(a);
        String password = conf.getUserPassword(a.getID()).second();
        conf.logoutNonAdmins(false);
        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() != LoginResponse.Valid){
            fail("Admins should not get logged out");
        }

    }

    @Test
    public void loginTwice(){
        Attendee a = new Attendee("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAttendee(a);
        String password = conf.getUserPassword(a.getID()).second();

        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() != LoginResponse.Valid){
            fail("First login should succeed");
        }

        response = conf.login(a.userName, password);
        if(response.first() == LoginResponse.Valid){
            fail("Second login should not have succeed");
        }

    }


    @Test
    public void loginTwice2(){
        Admin a = new Admin("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAdmin(a);
        String password = conf.getUserPassword(a.getID()).second();

        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password);
        if(response.first() != LoginResponse.Valid){
            fail("First login should succeed");
        }

        response = conf.login(a.userName, password);
        if(response.first() != LoginResponse.Valid){
            fail("Second login should have succeed");
        }

    }


    @Test
    public void loginTwice3(){
        Admin a = new Admin("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        conf.addAdmin(a);
        String password = conf.getUserPassword(a.getID()).second();

        Pair<LoginResponse, Pair<String, Long>> response = conf.login(a.userName, password+"a");
        if(response.first() == LoginResponse.Valid){
            fail("Managed to login with an invalid password");
        }

        response = conf.login(a.userName, password);
        if(response.first() != LoginResponse.Valid){
            fail("Second login should have succeed");
        }

    }

    @Test
    public void testTokens(){
        Admin a = new Admin("Mike", "Mike@Gebirge.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
        Attendee b = new Attendee("Mike", "Mike@Gebirge2.tods", conf.getFreeUserName("Mike"), "JUSO", "MPI", "SysAdmin");
        Attendee c = new Attendee("Mike", "Mike@Gebirge3.tods", conf.getFreeUserName("Mike"), "AI", "MPI", "SysAdmin");

        ArrayList<String> groups = new ArrayList<>();
        groups.add("RCDS");
        groups.add("JUSO");
        groups.add("AI");

        conf.addAdmin(a);
        conf.addAttendee(b);
        conf.addAttendee(c);

        if(!groups.containsAll(conf.getExistingGroups()) || !conf.getExistingGroups().containsAll(groups)){
            fail("Groups do not match");
        }

        String passwordA = conf.getUserPassword(a.getID()).second();

        Pair<LoginResponse, Pair<String, Long>> responseA = conf.login(a.userName, passwordA);

        String passwordB = conf.getUserPassword(b.getID()).second();

        Pair<LoginResponse, Pair<String, Long>> responseB = conf.login(b.userName, passwordB);

        String passwordC = conf.getUserPassword(c.getID()).second();

        Pair<LoginResponse, Pair<String, Long>> responseC = conf.login(c.userName, passwordC);

        conf.removeAttendee(c.getID());

        if(conf.checkToken(responseA.second().first()) != TokenResponse.ValidAdmin){
            fail("Expected an admin token");
        }

        if(conf.checkToken(responseB.second().first()) != TokenResponse.ValidAttendee){
            fail("Expected an attendee token");
        }

        if(conf.checkToken(responseC.second().first()) != TokenResponse.TokenDoesNotExist){
            fail("Expected an invalid token");
        }

    }

    @Test
    public void tokenReset(){
        Attendee a = new Attendee("Mike", "Mike@Gebirge2.tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");

        conf.addAttendee(a);
        String passwordA = conf.getUserPassword(a.getID()).second();

        Pair<LoginResponse, Pair<String, Long>> responseA = conf.login(a.userName, passwordA);

        if(conf.checkToken(responseA.second().first()) != TokenResponse.ValidAttendee){
            fail("Token should be valid at this point");
        }

        conf.generateNewUserToken(a.getID());

        if(conf.checkToken(responseA.second().first()) != TokenResponse.TokenDoesNotExist){
            fail("Token should be invalid at this point");
        }


    }

}