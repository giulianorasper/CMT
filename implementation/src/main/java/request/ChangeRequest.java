package request;

public class ChangeRequest extends Request {

    private boolean approved;
    private String message;

    protected ChangeRequest(int id) {
        super(id);
    }

    @Override
    public void reopen() {
        //TODO: Implement this
    }

    @Override
    public void close() {
        //TODO: Implement this
    }
}
