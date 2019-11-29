package user;

public class Attendee extends User {

    private String function;

    public int numberOfDevices() {
        return 0; //TODO: Implement this
    }

    public boolean isPresent() {
        return false; //TODO: Implement this
    }

    public String getFunction() {
        return this.function;
    }

    void setFunction(String function) {
        this.function = function;
    }
}
