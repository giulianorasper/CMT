package database;

public class Db_VoteManager  extends DbController implements Db_Vote_Interface {
	
	private static String table = "votes";

	public Db_VoteManager(String url) {
		this.url = url;
	}

	@Override
	public boolean voteExists(String votename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addVote(String votename, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editVote(String votename, int vote_id, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteVote(String votename, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getAllVote() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	
	
}
