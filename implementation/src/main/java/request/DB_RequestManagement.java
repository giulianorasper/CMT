package request;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public interface DB_RequestManagement extends RequestObserver{

    /**
     * Adds a new {@link Request} to the database.
     *
     * @param req The {@link Request} to be added.
     * @return True, iff the {@link Request} was successfully added.
     */
    boolean addRequest(Request req);

    /**
     * Reconstructs a given {@link Request} from the database.
     *
     * @param ID The ID of the {@link Request}.
     * @return the reconstructed {@link Request}.
     */
    Request getRequest(int ID);

    /**
     *
     * @return a list of all reconstructed {@link Request}s from the database.
     */
    List<Request> getAllRequests();

    /**
     * Updates the {@link Request} after the {@link RequestObservable} was changed.
     *
     * @param r The updates {@link Request}.
     * @return True, iff the updates was successful.
     */
     boolean update(Request r);
}
