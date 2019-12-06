package user;

import com.google.gson.annotations.Expose;

public class Attendee extends User {

    @Expose
    private String function;
    private int numberOfDevices;
    @Expose
    private boolean present;

    public Attendee(String name, String email, String userName, String group, String residence, String function, int ID){
        super(name, email, userName, group, residence, ID);
        this.function = function;

        this.numberOfDevices = 0;
        this.present = false;
    }

    //TODO implement this constructor
    public Attendee(String name, String email, String group, String residence, String function) {
        this(null,null,null,null,null,null, -1);
    }

    public void additionalDevice(){
        this.numberOfDevices++;
    }

    public void attendedConference(){
        this.present = true;
    }

    public void logout(){
        this.present = false;
    }

    public boolean isPresent() {
        return present;
    }

    public int getNumberOfDevices() {
        return this.numberOfDevices;
    }

    public String getFunction() {
        return this.function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
