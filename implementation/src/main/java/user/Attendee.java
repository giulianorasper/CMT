package user;

public class Attendee extends User {

    private String function;
    private int numberOfDevices;
    private boolean present;

    public Attendee(String name, String group, String residence, String function, int ID){
        this.name = name;
        this.group = group;
        this.residence = residence;
        this.function = function;
        this.ID = ID;

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
