package user;

import com.google.gson.annotations.Expose;

public class Attendee extends User {

    @Expose
    private String function;
    private int numberOfDevices;
    @Expose
    private boolean present;

    public Attendee(String name, String userName, String group, String residence, String function, int ID){
        super(name, userName, group, residence, ID);
        this.function = function;

        this.numberOfDevices = 0;
        this.present = false;
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

    void setFunction(String function) {
        this.function = function;
    }
}
