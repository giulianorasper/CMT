package database;

import voting.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_VotingManager extends DB_Controller implements DB_VotingManagement {

    public DB_VotingManager(String url) {
        super(url);
    }

    /**
     * Initializes the agenda tables for the database
     */
    @Override
    protected void init() {
        String votingsTable = "CREATE TABLE IF NOT EXISTS votings (\n"
                + "     votingID INTEGER PRIMARY KEY, \n"
                + "     isNamed BOOL, \n"
                + "     numberOfOptions, \n"
                + "     question TEXT NOT NULL, \n"
                + "     tableName TEXT NOT NULL UNIQUE \n"
                + ") WITHOUT ROWID;";

        openConnection();
        try {
            connection.createStatement().execute(votingsTable);
        } catch (SQLException e) {
            System.err.println("Database initialization failed!");
            System.err.println(e.getMessage());
        }
        closeConnection();
    }

    /**
     * Adds a new, already finished, {@link Voting} to the database.
     *
     * @param v The {@link Voting} to be added.
     * @return True, iff the {@link Voting} was successfully added. False if a voting with that ID did already exist.
     */
    @Override
    public boolean addVoting(Voting v) {
        this.openConnection();
        String duplicate = "SELECT * FROM votings WHERE votingID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(duplicate)) {
            stmt.setInt(1, v.getID());
            try (ResultSet table = stmt.executeQuery()) {
                if (!table.isAfterLast()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            this.closeConnection();
            System.err.println("An error occurred while trying to check a voting duplicate.");
        }
        String sqlstatement = "INSERT INTO votings (votingID, isNamed, numberOfOptions, question, tableName) "
                + " VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setInt(1, v.getID());
            stmt.setBoolean(2, v.isNamedVote());
            stmt.setInt(3, v.getOptions().size());
            stmt.setString(4, v.getQuestion());
            stmt.setString(5, "voting" + v.getID());
            stmt.executeUpdate();
            if (v.isNamedVote()) {
                String votingTable = "CREATE TABLE IF NOT EXISTS voting" + Integer.toString(v.getID()) + " ("
                        + "     optionID INTEGER, "
                        + "     optionName TEXT, "
                        + "     userID INTEGER UNIQUE );";
                connection.createStatement().execute(votingTable);
                for (VotingOption p : v.getOptions()) {
                    List<Integer> voters = p.getVoters();
                    for (Integer i : voters) {
                        String insert = "INSERT INTO voting" + Integer.toString(v.getID()) +
                                " (optionID, optionName, userID) VALUES (?,?,?)";
                        try (PreparedStatement in = connection.prepareStatement(insert)) {
                            in.setInt(1, p.getOptionID());
                            in.setString(2, p.getName());
                            in.setInt(3, i);
                            in.executeUpdate();
                        }
                    } if (voters.isEmpty()) { // In case noone voted for this option.
                        String insert = "INSERT INTO voting" + Integer.toString(v.getID()) +
                                " (optionID, optionName, userID) VALUES (?,?,?)";
                        try (PreparedStatement in = connection.prepareStatement(insert)) {
                            in.setInt(1, p.getOptionID());
                            in.setString(2, p.getName());
                            in.setNull(3, Types.INTEGER);
                            in.executeUpdate();
                        }
                    }
                }
            } else {
                String votingTable = "CREATE TABLE IF NOT EXISTS voting" + Integer.toString(v.getID()) + " ("
                        + "     optionID INTEGER PRIMARY KEY, "
                        + "     optionName  BOOL, "
                        + "     result INTEGER "
                        + ") WITHOUT ROWID ;";
                connection.createStatement().execute(votingTable);
                for (VotingOption p : v.getOptions()) {
                    String insert =
                            "INSERT INTO voting" + Integer.toString(v.getID()) +
                                    " (optionID, optionName, result) VALUES (?,?,?)";
                    try (PreparedStatement in = connection.prepareStatement(insert)) {
                        in.setInt(1, p.getOptionID());
                        in.setString(2, p.getName());
                        in.setInt(3, p.getCurrentResult());
                        in.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("An error occurred while adding a new voting to the database.");
            System.err.println(ex.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    /**
     * Reconstructs a given {@link Voting} from the database.
     *
     * @param ID The ID of the {@link Voting}.
     * @return the reconstructed {@link Voting}.
     */
    @Override
    public Voting getVoting(int ID) {
        this.openConnection();
        Voting voting = null;
        String sqlstatement = "SELECT isNamed, numberOfOptions, question, tableName FROM votings"
                + " WHERE votingID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setInt(1, ID);
            try (ResultSet table = stmt.executeQuery()) {
                String optionRequest = "SELECT * FROM  " + table.getString("tableName");
                try (PreparedStatement optR = connection.prepareStatement(optionRequest);
                     ResultSet vot = optR.executeQuery()) {
                    List<VotingOption> options = new ArrayList<>();
                    if (table.getBoolean("isNamed")) {
                        //TODO: Implement me
                    } else {
                        while (vot.next()) {
                            VotingOption v =
                                    new AnonymousVotingOption(vot.getInt("optionID"),
                                            vot.getString("optionName"),
                                            vot.getInt("result"));
                            options.add(v);
                        }
                    }
                    voting = new Voting(options,
                            table.getString("question"), ID, table.getBoolean("isNamed"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("An error occurred while reconstructing a voting from the database.");
            System.err.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return voting;
    }

    /**
     *
     * @return a list of all reconstructed {@link Voting}s from the database.
     */
    @Override
    public List<Voting> getVotings() {
        this.openConnection();
        List<Voting> votings = new LinkedList<>();
        String sqlstatement = "SELECT * FROM votings";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement);
             ResultSet table  = stmt.executeQuery()) {
            while (table.next()) {
                votings.add(this.getVoting(table.getInt("votingID")));
            }
        } catch (SQLException ex) {
            System.err.println("An error occurred while reconstructing all voting from the database.");
            System.err.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return votings;
    }

    /**
     * Updates the {@link Voting} after the {@link VotingObservable} was changed.
     *
     * @param v The updates {@link Voting}.
     * @return True, iff the updates was successful.
     */
    @Override
    public boolean update(Voting v) {
        if (v.getStatus() == VotingStatus.Closed) {
            this.addVoting(v);
            return true;
        } return true;
    }
}
