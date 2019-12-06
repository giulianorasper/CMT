package user;

public class Admin extends Attendee {

    public boolean isLogedIn() {
        try {
            lock.getReadAccess();
            return logedIn;
        }
        catch (InterruptedException e){
            return false;
        }
        finally {
            lock.finishRead();
        }
    }

    public void login() {
        this.logedIn = true;
    }

    public void logout() {
        this.logedIn = false;
    }

    private boolean logedIn;

    public Admin(String name, String email, String userName, String group, String residence, String function, int ID){
        super(name, email, userName, group, residence, function, ID);
    }

    public Admin(String name, String email, String userName, String group, String residence, String function){
        this(name, email, userName, group, residence, function, nextFreeId());
    }
}
