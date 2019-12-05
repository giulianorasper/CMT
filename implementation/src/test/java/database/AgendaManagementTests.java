package database;

import agenda.Agenda;
import agenda.DB_AgendaManagement;
import agenda.Topic;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AgendaManagementTests extends DatabaseTests {

    @Test
    public void addValidTopic(){
        DB_AgendaManagement agendaDB = this.getAgendaDB();
        Agenda agenda = new Agenda();
        Topic firstTopic = new Topic("Käsebrot", agenda);
        Topic firstSubTopic = new Topic("Käse", firstTopic.getSubTopics());
        Topic secondSubTopic = new Topic("Brot", firstTopic.getSubTopics());

        agenda.addTopic(firstTopic,0);
        firstTopic.getSubTopics().addTopic(firstSubTopic, 0);
        firstTopic.getSubTopics().addTopic(secondSubTopic, 1);

        agendaDB.update(agenda);

        Agenda reference = agendaDB.getAgenda();

        Topic refFirstTopic = reference.getTopic(0);
        Topic refFirstSubTopic = reference.getTopic(0).getSubTopics().getTopic(0);
        Topic refSecondSubTopic = reference.getTopic(0).getSubTopics().getTopic(1);

        assertEquals("First topic does not match", firstTopic.getName(), refFirstTopic.getName());
        assertEquals("First subtopic does not match", firstSubTopic.getName(), refFirstSubTopic.getName());
        assertEquals("Second subtopic does not match", secondSubTopic.getName(),refSecondSubTopic.getName());
    }

}
