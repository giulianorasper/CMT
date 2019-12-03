import agenda.Agenda;
import agenda.Topic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import org.junit.rules.ExpectedException;

public class AgendaTests {

    private Agenda agenda;

    @Before
    public void setupAgenda() {
        agenda = new Agenda();
        Topic t1 = new Topic("Topic 1", agenda);
        Topic t2 = new Topic("Topic 2", agenda);
        Topic t3 = new Topic("Topic 3", agenda);
        Topic t4 = new Topic("Topic 4", agenda);
        agenda.addTopic(t1, 0);
        agenda.addTopic(t2, 1);
        agenda.addTopic(t3, 2);
        agenda.addTopic(t4, 3);
        Topic t2_1 = new Topic("Subtopic of 2", t2.getSubTopics());
        t2.getSubTopics().addTopic(t2_1,0);
        Topic t3_1 = new Topic("First Subtopic of 3", t3.getSubTopics());
        Topic t3_2 = new Topic("Second Subtopic of 3", t3.getSubTopics());
        Topic t3_3 = new Topic("Third Subtopic of 3", t3.getSubTopics());
        t3.getSubTopics().addTopic(t3_1, 0);
        t3.getSubTopics().addTopic(t3_2, 1);
        t3.getSubTopics().addTopic(t3_3, 2);
        Topic t3_2_1 = new Topic("First Subtopic of the second subtopic of 3", t3_2.getSubTopics());
        t3_2.getSubTopics().addTopic(t3_2_1, 0);
    }

    @Test
    public void testGetTopicFromPreorderString() {
        String expected1 = "Topic 1";
        String expected2 = "Topic 3";
        String expected3 = "Topic 4";
        String expected4 = "First Subtopic of 3";
        String expected5 = "Second Subtopic of 3";
        String expected6 = "First Subtopic of the second subtopic of 3";

        Assert.assertEquals(expected1, agenda.getTopicFromPreorderString("1").getName());
        Assert.assertEquals(expected2, agenda.getTopicFromPreorderString("3").getName());
        Assert.assertEquals(expected3, agenda.getTopicFromPreorderString("4").getName());
        Assert.assertEquals(expected4, agenda.getTopicFromPreorderString("3.1").getName());
        Assert.assertEquals(expected5, agenda.getTopicFromPreorderString("3.2.").getName());
        Assert.assertEquals(expected6, agenda.getTopicFromPreorderString("3.2.1").getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTopicFromPreorderStringInvalidArgs1() {
        agenda.getTopicFromPreorderString("0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTopicFromPreorderStringInvalidArgs2() {
        agenda.getTopicFromPreorderString("5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTopicFromPreorderStringInvalidArgs3() {
        agenda.getTopicFromPreorderString("2.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTopicFromPreorderStringInvalidArgs4() {
        agenda.getTopicFromPreorderString("Never gonna give you up");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTopicFromPreorderStringInvalidArgs5() {
        agenda.getTopicFromPreorderString("Never gonna let you down. Never gonna run around and desert you");
    }

}
