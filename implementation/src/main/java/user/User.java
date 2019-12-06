package user;

import com.google.gson.annotations.Expose;
import utils.WriterBiasedRWLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class User {

    @Expose
    protected String name;

    @Expose
    protected final String userName;
    @Expose
    protected String group;
    @Expose
    protected String function;
    @Expose
    protected String residence;
    @Expose
    protected String email;
    @Expose
    protected final int ID;

    private static int idAt = 0;
    private static Lock idsLock = new ReentrantLock();
    protected WriterBiasedRWLock lock = new WriterBiasedRWLock();

    protected static int nextFreeId(){
        try{
            idsLock.lock();
            idAt++;
            return idAt;
        }
        finally {
            idsLock.unlock();
        }
    }

    public User(String name, String email, String userName, String group, String function, String residence, int ID){
        this.name = name;
        this.email = email;
        this.group = group;
        this.residence = residence;
        this.userName = userName;
        this.function = function;
        try{
            idsLock.lock();
            this.ID = ID;
            if(ID > idAt){
                idAt = ID;
            }
        }
        finally {
            idsLock.unlock();
        }
    }
    

    public String getName() {
        try {
            lock.getReadAccess();
            return name;
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public void setName(String name) {
        try {
            lock.getWriteAccess();
            this.name = name;
        }
        catch (InterruptedException e){
            //do nothing
        }
        finally {
            lock.finishWrite();
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getGroup() {
        try {
            lock.getReadAccess();
            return group;
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public void setGroup(String group) {
        try {
            lock.getWriteAccess();
            this.group = group;
        }
        catch (InterruptedException e){
            //do nothing
        }
        finally {
            lock.finishWrite();
        }
    }

    public String getFunction() {
        try {
            lock.getReadAccess();
            return function;
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public void setFunction(String function) {
        try {
            lock.getWriteAccess();
            this.function = function;
        }
        catch (InterruptedException e){
            //do nothing
        }
        finally {
            lock.finishWrite();
        }
    }

    public String getResidence() {
        try {
            lock.getReadAccess();
            return residence;
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public void setResidence(String residence) {
        try {
            lock.getWriteAccess();
            this.residence = residence;
        }
        catch (InterruptedException e){
            //do nothing
        }
        finally {
            lock.finishWrite();
        }
    }

    public int getID() {
        return ID;
    }

    public String getEmail() {
        try {
            lock.getReadAccess();
            return email;
        }
        catch (InterruptedException e){
            return null;
        }
        finally {
            lock.finishRead();
        }
    }

    public void setEmail(String email) {
        try {
            lock.getWriteAccess();
            this.email = email;
        }
        catch (InterruptedException e){
            //do nothing
        }
        finally {
            lock.finishWrite();
        }
    }
}
