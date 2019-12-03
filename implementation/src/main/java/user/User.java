package user;

import com.google.gson.annotations.Expose;

public abstract class User {

    @Expose
    protected String name;
    @Expose
    protected String group;
    @Expose
    protected String residence;
    @Expose
    protected String email;
    @Expose
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

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }
}
