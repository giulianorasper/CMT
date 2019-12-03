package database;

import user.*;
import utils.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("checkstyle:typename")
public class DB_GeneralUserManager extends DB_Controller implements DB_GeneralUserManagement {

    public DB_GeneralUserManager(String url) {
        super(url);
    }

    @Override
    public Pair<LoginResponse, String> checkLogin(String userName, String password) {
        this.openConnection();

        String sqlstatement = "SELECT * FROM users WHERE fullname = ? ";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, userName);
            ResultSet table  = stmt.executeQuery();
            if (!table.next()) {
                return new Pair<>(LoginResponse.UserDoesNotExist, null);
            } else {
                if (table.getString("password") == null) {
                    return new Pair<>(LoginResponse.PasswordAlreadyUsed, null);
                } else {
                    if (table.getString("token") == null) {
                        return new Pair<>(LoginResponse.AccountBlocked, null);
                    } else if (table.getString("password") != password) {
                        return new Pair<>(LoginResponse.WrongPassword, null);
                    } else {
                        return new Pair<>(LoginResponse.Valid, table.getString("token"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
    }

    @Override
    public TokenResponse checkToken(String token) {
        this.openConnection();

        String sqlstatement = "SELECT * FROM users WHERE token = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, token);
            ResultSet table  = stmt.executeQuery();
            if (!table.next()) {
                return TokenResponse.TokenDoesNotExist;
            } else {
                if (table.getString("token") == null &&
                    table.getString("password") == null) {
                    return TokenResponse.AccountBlocked;
                } else {
                    if (table.getBoolean("isAdmin")) {
                        return TokenResponse.ValidAdmin;
                    } else {
                        return TokenResponse.ValidAttendee;
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
    }

    @Override
    public boolean removeUser(int userID) {
        this.openConnection();

        String sqlstatement = "DELETE FROM users"
                + " WHERE userID = ?";
        try  {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setInt(1, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    @Override
    public boolean logoutUser(int userID) {
        this.openConnection();

        String sqlstatement = "UPDATE users SET password = ? , token = ? "
                + " WHERE userID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setNull(1, Types.VARCHAR);
            stmt.setNull(2, java.sql.Types.VARCHAR);
            stmt.setInt(3, userID);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    @Override
    public List<Pair<User, String>> getAllPasswords() {
        this.openConnection();

        List<Pair<User, String>> pass = new LinkedList<>();
        String sqlstatement = "SELECT * FROM users";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            ResultSet att  = stmt.executeQuery();
            while (att.next()) {
                User user = new Attendee(att.getString("fullname"),
                        att.getString("username"),
                        att.getString("groups"),
                        att.getString("residence"),
                        att.getString("function"),
                        att.getInt("userID"));
                pass.add(new Pair<>(user, att.getString("password")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return pass;
    }

    @Override
    public boolean storeNewToken(int userID, String token) {
        this.openConnection();

        String sqlstatement = "UPDATE users SET token = ? , "
                + " WHERE userID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, token);
            stmt.setInt(2, userID);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    @Override
    public boolean storeNewPassword(int userID, String password) {
        this.openConnection();

        String sqlstatement = "UPDATE users SET password = ? , "
                + " WHERE userID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, password);
            stmt.setInt(2, userID);
            stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    @Override
    public boolean userNameAlreadyUsed(String username) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM users WHERE username = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            stmt.setString(1, username);
            ResultSet table  = stmt.executeQuery();
            if (!table.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
    }

    @Override
    public List<Integer> getIDs() {
        this.openConnection();
        List<Integer> IDs = new LinkedList<>();

        String sqlstatement = "SELECT userID FROM users";
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlstatement);
            ResultSet table  = stmt.executeQuery();
            while (table.next()) {
                IDs.add(table.getInt("userID"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return IDs;
    }
}
