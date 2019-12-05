package database;

import agenda.Agenda;
import agenda.AgendaObserver;
import agenda.DB_AgendaManagement;
import agenda.Topic;
import utils.LexicographicalComparator;
import utils.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("checkstyle:typename")
public class DB_AgendaManager extends DB_Controller implements DB_AgendaManagement {

    private static String table = "agenda";

    public DB_AgendaManager(String url) {
        super(url);
    }

    @Override
    protected void init() {
        String agendaTable = "CREATE TABLE IF NOT EXISTS agenda (\n"
                + "     topicPosition TEXT NOT NULL,\n"
                + "     topicName TEXT NOT NULL\n"
                + ");";
        openConnection();
        try {
            connection.createStatement().execute(agendaTable);
        } catch (SQLException e) {
            System.err.println("Database initialization failed!");
            System.err.println(e.getMessage());
        }
        closeConnection();
    }

    @Override
    public boolean update(Agenda a) {
        this.openConnection();
        List<String> preOrder = a.preOrder();
        try {
            for (String s : preOrder) {
                String name = a.getTopicFromPreorderString(s).getName();
                String sqlstatement = "INSERT INTO agenda(topicPosition, topicName)"
                        + "VALUES(?,?)";
                PreparedStatement stmt = connection.prepareStatement(sqlstatement);
                stmt.setString(1, s);
                stmt.setString(2, name);
                stmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }


    @Override
    public Agenda getAgenda() {
        this.openConnection();
        String sqlstatement = "SELECT * FROM agenda";
        Agenda ag = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            ResultSet agenda  = stmt.executeQuery();
            List<Pair<List<Integer>, String>> tops = new LinkedList<>();
            while (agenda.next()) {
                String ord = agenda.getString("topicPosition");
                List<Integer> order = new LinkedList<Integer>();
                Arrays.asList(ord.split(" ")).forEach(s -> order.add(Integer.parseInt(s)));
                String name = agenda.getString("topicName");
                tops.add(new Pair<>(order, name));
            }
            return new Agenda(tops);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            this.closeConnection();
        }
        return ag;
    }
}
