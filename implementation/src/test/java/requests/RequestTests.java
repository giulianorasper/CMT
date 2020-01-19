package requests;

import agenda.Agenda;
import agenda.Topic;
import main.Conference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import request.ChangeRequest;
import request.Request;
import request.SpeechRequest;
import user.Attendee;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.fail;

public class RequestTests {

    Conference conf;
    Attendee  testAttendee;
    @Before
    public void createConference(){
        Agenda agenda = new Agenda();
        Topic t = new Topic("Topic 1", agenda);
        agenda.addTopic(t, 0);
        conf = new Conference(true);
        conf.updateAgenda(agenda);
        testAttendee = new Attendee("test", "test", "test", "test", "test", "test");
        conf.addAttendee(testAttendee);
    }

    @Test
    public void multipleRequests(){
        int requestCount =  20;

        for(int i = 0; i<requestCount; i++){
            Request req = new ChangeRequest(testAttendee, conf.getAgenda().getTopic(0), System.currentTimeMillis(),"more " +
                    "tests");
            conf.addRequest(req);
        }

        Assert.assertEquals("Not all requests got logged", requestCount, conf.getAllRequests().size());
    }

    @Test
    public void deleteTop(){
            Request req = new SpeechRequest(testAttendee, conf.getAgenda().getTopic(0), System.currentTimeMillis());
            conf.addRequest(req);
            Agenda agenda = new Agenda();
            conf.updateAgenda(agenda);
            Assert.assertEquals("The request got removed", 1, conf.getAllRequests().size());
            Assert.assertEquals("Wrong request name","Topic 1", conf.getAllRequests().get(0).getRequestable().getRequestableName());
    }

    @Test
    public void deleteDocument(){

        String pathString = "src/test/resources/test.txt";
        File f = new File(pathString);
        System.out.println(f.getAbsoluteFile());
        if(f.exists()){
            f.delete();
        }

        try {
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathString));
            writer.write("Test data\n");

            writer.close();
        } catch (IOException e) {
            fail("Could not initialize test environment");
        }

        conf.updateDocument("test", "txt", f, true);
        Request req = new SpeechRequest(testAttendee, conf.getDocument("test"), System.currentTimeMillis());
        conf.addRequest(req);
        Agenda agenda = new Agenda();
        conf.deleteDocument("test");
        Assert.assertEquals("The request got removed", 1, conf.getAllRequests().size());
        Assert.assertEquals("Wrong request name","test", conf.getAllRequests().get(0).getRequestable().getRequestableName());
    }

    @Test
    public void deleteUser(){
        Request req = new SpeechRequest(testAttendee, conf.getAgenda().getTopic(0), System.currentTimeMillis());
        conf.addRequest(req);
        Agenda agenda = new Agenda();
        conf.removeAttendee(testAttendee.getID());
        Assert.assertEquals("The request got removed", 1, conf.getAllRequests().size());
        Assert.assertEquals("Wrong user name","Topic 1", conf.getAllRequests().get(0).getRequester().getName());
    }

}
