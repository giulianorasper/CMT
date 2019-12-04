package database;

import document.Document;
import voting.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_VotingManager extends DB_Controller implements DB_VotingManagement {

    private static String table = "votings";

    public DB_VotingManager(String url) {
        super(url);
    }

    @Override
    public boolean addVoting(Voting v) {
        this.openConnection();
        String sqlstatement = "INSERT INTO votings(votingID, isNamed, numberOfOptions, question, tableName)"
                + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setInt(1, v.getID());
            stmt.setBoolean(2, v.isNamedVote());
            stmt.setInt(3, v.getOptions().size());
            stmt.setString(4, v.getQuestion());
            stmt.setString(5, "voting" + v.getID());
            stmt.executeUpdate();

            if (v.isNamedVote()) {
                String votingTable = "CREATE TABLE IF NOT EXISTS voting" + v.getID() + " (\n"
                        + "     optionID INTEGER, \n"
                        + "     optionName TEXT, \n"
                        + "     userID INTEGER UNIQUE \n"
                        + ");";
                connection.createStatement().execute(votingTable);
                for (VotingOption p : v.getOptions()) {
                    List<Integer> voters = p.getVoters();
                    for (Integer i : voters) {
                        String insert = "INSERT INTO voting" + v.getID() + "(optionID, optionName, userID)"
                                + "VALUES(?,?,?)";
                        PreparedStatement in = connection.prepareStatement(insert);
                        in.setInt(1, p.getOptionID());
                        in.setString(2, p.getName());
                        in.setInt(3, i);
                        in.executeUpdate();
                    }
                }
            } else {
                String votingTable = "CREATE TABLE IF NOT EXISTS voting" + v.getID() + " (\n"
                        + "     optionID INTEGER PRIMARY KEY, \n"
                        + "     optionName  BOOL, \n"
                        + "     result INTEGER \n"
                        + ") WITHOUT ROWID ;";
                connection.createStatement().execute(votingTable);
                for (VotingOption p : v.getOptions()) {
                    String insert = "INSERT INTO voting" + v.getID() + "(optionID, optionName, result)"
                            + "VALUES(?,?,?)";
                    PreparedStatement in = connection.prepareStatement(insert);
                    in.setInt(1, p.getOptionID());
                    in.setString(2, p.getName());
                    in.setInt(3, p.getCurrentResult());
                    in.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    @Override
    public Voting getVoting(int ID) {
        this.openConnection();
        Voting voting = null;
        try {
            String sqlstatement = "SELECT isNamed, numberOfOptions, question, tableName FROM votings"
                    + " WHERE votingID = ?";
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setInt(1, ID);
            ResultSet table  = stmt.executeQuery();

            String optionRequest = "SELECT * FROM " + table.getString("tableName");
            PreparedStatement optR = connection.prepareStatement(optionRequest);
            ResultSet vot = optR.executeQuery();

            List<VotingOption> options = new ArrayList<>();
            if (table.getBoolean("isNamed")) {
                List<List<Integer>> res = new ArrayList<>();
                String[] map = new String[table.getInt("numberOfOptions")];
                for (int i = 0; i < table.getInt("numberOfOptions"); i++) {
                    res.add(new LinkedList<>());
                }
                while (vot.next()) {
                    if (map[vot.getInt("optionID")] == null) {
                        map[vot.getInt("optionID")] = vot.getString("optionName");
                    }
                    res.get(vot.getInt("optionID")).add(vot.getInt("userID"));
                }
                for (List<Integer> p : res) {
                    VotingOption v = new NamedVotingOption(p);
                    v.changeName(map[res.indexOf(p)]);
                    v.setOptionID(res.indexOf(p));
                    options.add(v);
                }
            } else {
                while (vot.next()) {
                    VotingOption v = new AnonymousVotingOption(vot.getInt("result"));
                    v.changeName(vot.getString("optionName"));
                    v.setOptionID(vot.getInt("optionID"));
                    options.add(v);
                }
            }
            voting = new Voting(options, table.getString("question"), table.getInt("votingID"));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return voting;
    }

    @Override
    public List<Voting> getVotings() {
        this.openConnection();
        List<Voting> votings = new LinkedList<>();
        try {
            String sqlstatement = "SELECT * FROM votings";
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            ResultSet table  = stmt.executeQuery();

            while (table.next()) {
                votings.add(this.getVoting(table.getInt("votingID")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return votings;
    }
}
