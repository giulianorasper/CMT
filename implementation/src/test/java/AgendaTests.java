import agenda.Agenda;
import agenda.Topic;

import org.junit.Before;
import org.junit.Test;
import utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

        assertEquals(expected1, agenda.getTopicFromPreorderString("1").getName());
        assertEquals(expected2, agenda.getTopicFromPreorderString("3").getName());
        assertEquals(expected3, agenda.getTopicFromPreorderString("4").getName());
        assertEquals(expected4, agenda.getTopicFromPreorderString("3.1").getName());
        assertEquals(expected5, agenda.getTopicFromPreorderString("3.2.").getName());
        assertEquals(expected6, agenda.getTopicFromPreorderString("3.2.1").getName());
    }

    @Test
    public void testAgendaConstructorOrderedListSimple() {
        List<Pair<List<Integer>, String>> tops = new LinkedList<>();
        tops.add(new Pair<>(Arrays.asList(3, 1), "Top 3.1"));
        tops.add(new Pair<>(Arrays.asList(3, 1, 1), "Top 3.1.1"));
        tops.add(new Pair<List<Integer>, String>(Arrays.asList(1), "Top 1"));
        tops.add(new Pair<List<Integer>, String>(Arrays.asList(2), "Top 2"));
        tops.add(new Pair<List<Integer>, String>(Arrays.asList(3), "Top 3"));
        tops.add(new Pair<List<Integer>, String>(Arrays.asList(4), "Top 4"));

        Agenda ag = new Agenda(tops);

        assertEquals("Preorder String invalid.",
                Arrays.asList("1", "2", "3", "3.1", "3.1.1", "4"),ag.preOrder());
        assertEquals("toString returning invalid results",
                "{Top 1 {}, Top 2 {}, Top 3 {Top 3.1 {Top 3.1.1 {}}}, Top 4 {}}",ag.toString());
    }

    @Test
    public void testPreorder(){
        Agenda agenda = new Agenda();
        Topic firstTopic = new Topic("Käsebrot", agenda);
        Topic firstSubTopic = new Topic("Käse", firstTopic.getSubTopics());
        Topic secondSubTopic = new Topic("Brot", firstTopic.getSubTopics());

        agenda.addTopic(firstTopic,0);
        firstTopic.getSubTopics().addTopic(firstSubTopic, 0);
        firstTopic.getSubTopics().addTopic(secondSubTopic, 1);

        List<Pair<List<Integer>, String>> tops = new ArrayList<>();
        List<Integer> first = new ArrayList<>();
        first.add(1);
        tops.add(new Pair<>(first, "Käsebrot"));
        List<Integer> second = new ArrayList<>();
        second.add(1);
        second.add(1);
        tops.add(new Pair<>(second, "Käse"));
        List<Integer> third = new ArrayList<>();
        third.add(1);
        third.add(2);
        tops.add(new Pair<>(third, "Brot"));

        Agenda secondAgenda = new Agenda(tops);

        assertEquals("Agenda differs:", agenda.preOrder(), secondAgenda.preOrder());
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
