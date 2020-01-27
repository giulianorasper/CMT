package database;

public class Db_RequestManager extends DbController implements Db_Request_Interface {
	
	private static String table = "requests";

	public Db_RequestManager(String url) {
		this.url = url;
	}

	@Override
	public boolean requestExists(String requestname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addRequest(String requestname, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editRequest(String requestname, int request_id, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRequest(String docname, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getAllRequests() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	
	
}
