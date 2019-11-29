package user;

public abstract class User {

    protected String name;
    protected String group;
    protected String residence;
    protected int ID;

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    void setGroup(String group) {
        this.group = group;
    }

    public String getResidence() {
        return residence;
    }

    void setResidence(String residence) {
        this.residence = residence;
    }

    public int getID() {
        return ID;
    }
}
