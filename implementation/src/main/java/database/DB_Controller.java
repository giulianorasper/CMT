package database;

public abstract class DB_Controller {

    public abstract void init();

    public abstract void openConnection();

    public abstract void closeConnection();
}
