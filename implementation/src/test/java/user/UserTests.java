package user;

import main.Conference;
import org.junit.Before;
import org.junit.Test;
import utils.Pair;

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
            Attendee a = new Attendee("Mike", "Mike@Gebirge"+i+".tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
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

                    assertEquals("Attendee has different name despite successful edit " + a,  "Mike" + aux.get(),a.name);
                    assertEquals("Attendee has different residence despite successful edit " + a, "MPI" + aux.get(), a.residence);



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
        int threadCount = 20;
        AtomicInteger doneCount = new AtomicInteger(0);
        AtomicBoolean go = new AtomicBoolean(false);

        int[] attendeeIds = new int[threadCount];

        for(int  i = 0; i < threadCount; i++){
            Attendee a = new Attendee("Mike", "Mike@Gebirge"+i+".tods", conf.getFreeUserName("Mike"), "RCDS", "MPI", "SysAdmin");
            conf.addAttendee(a);
            attendeeIds[i] = a.getID();
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
                        if (aux.get() % 2 == 0){
                            response = conf.login("Mike" + aux.get(), password);
                            if(response.first() != LoginResponse.Valid){
                                fail("Failed to perform a valid login");
                            }
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

        conf.logoutNonAdmins();
        loginCount = (int) conf.getAllAttendees().stream().filter(Attendee::isPresent).count();
        assertEquals("Expected aa attendees to be logged out", 0 , loginCount);

    }

}