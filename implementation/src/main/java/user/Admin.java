package user;

public class Admin extends Attendee {

    private boolean logedIn;


    /**
     * Construct an Admin object with the following attributes, especially with explicit ID:
     * @param name unique pre and lastname of the Admin
     * @param email unique email of the Admin
     * @param userName unique userName of the Admin (needed to login)
     * @param group of the Admin
     * @param residence unique residence of the Admin (String with plz city, street for example)
     * @param function of the Admin in the Conference
     * @param ID unique and not already used ID for the Admin
     */
    public Admin(String name, String email, String userName, String group, String residence, String function, int ID){
        super(name, email, userName, group, residence, function, ID);
    }

    /**
     * Construct an Admin object with the following attributes, especially without explicit ID:
     * Use the next free, unique ID in the Userdatabase to construct the Admin
     * @param name unique pre and lastname of the Admin
     * @param email unique email of the Admin
     * @param userName unique userName of the Admin (needed to login)
     * @param group of the Admin
     * @param residence unique residence of the Admin (String with plz city, street for example)
     * @param function of the Admin in the Conference
     */
    public Admin(String name, String email, String userName, String group, String residence, String function){
        this(name, email, userName, group, residence, function, nextFreeId());
    }

    /**
     * Check if a Admin is logged in in the conference.
     * @return true iff Admin is logged in
     */
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

    /**
     * Change that admin is loggedIn in the Conderence.
     */
    public void login() {
        this.logedIn = true;
    }

    /**
     * Logout admin - admin isn't logedIn in the conference.
     */
    public void logout() {
        this.logedIn = false;
    }

}
