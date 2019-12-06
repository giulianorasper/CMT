package user;

import com.google.gson.annotations.Expose;

public abstract class User {

    @Expose
    protected String name;

    public String getUserName() {
        return userName;
    }

    @Expose
    protected String userName;
    @Expose
    protected String group;
    @Expose
    protected String residence;
    @Expose
    protected String email;
    @Expose
    protected int ID;

    public User(String name, String email, String userName, String group, String residence, int ID){
        this.name = name;
        this.email = email;
        this.group = group;
        this.residence = residence;
        this.userName = userName;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
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

    public void setEmail(String email) {
        this.email = email;
    }
}
