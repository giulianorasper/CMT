package user;

public class Admin extends Attendee {

    public boolean isLogedIn() {
        return logedIn;
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
}
