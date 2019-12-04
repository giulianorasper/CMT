package database;

import agenda.Agenda;
import agenda.DB_AgendaManagement;
import agenda.Topic;
import org.junit.*;

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
    }

}
