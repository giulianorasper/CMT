package database;

public class Db_AgendaManager extends DbController implements Db_Agenda_Interface {
	
	private static String table = "agenda";

	public Db_AgendaManager(String url) {
		this.url = url;
	}

	@Override
	public boolean existTopic(String topicname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addTopic(String topicname, String sequence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renameTopic(String old_topicname, int topic_id, String new_name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeTopic(String topicname, int old_topic_id, String new_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTopic(String topicname, int topic_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getAllTopics() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
