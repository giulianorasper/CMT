package communication.wrapper;

public interface Connection {

    void send(String message);

    void close();
}
