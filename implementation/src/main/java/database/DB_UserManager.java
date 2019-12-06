package database;

import user.*;
import utils.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DB_UserManager extends DB_Controller implements DB_UserManagement {

    public DB_UserManager(String url) {
        super(url);
    }

    /**
     * Initializes the user table for the database
     */
    @Override
    protected void init() {
        String userTable = "CREATE TABLE IF NOT EXISTS users (\n"
                + "     userID INTEGER PRIMARY KEY,\n"
                + "     fullname TEXT NOT NULL,\n"
                + "     username TEXT NOT NULL UNIQUE,\n"
                + "     password TEXT UNIQUE,\n"
                + "     token TEXT UNIQUE,\n"
                + "     email TEXT NOT NULL UNIQUE,\n"
                + "     groups TEXT NOT NULL,\n"
                + "     function TEXT NOT NULL,\n"
                + "     residence TEXT NOT NULL UNIQUE,\n"
                + "     isAdmin BOOL NOT NULL,\n"
                + "     present BOOL NOT NULL\n"
                + ") WITHOUT ROWID;";
        openConnection();
        try {
            connection.createStatement().execute(userTable);
        } catch (SQLException e) {
            System.err.println("Database initialization failed!");
            System.err.println(e.getMessage());
        }
        closeConnection();
    }

    /**
     * This method checks whether a pair of a username and a password is a valid combination in the database.
     *
     * @param userName The unique userName of the given User.
     * @param password The password to be checked.
     * @return A {@link LoginResponse} indicating the result and a token, in case the login was valid.
     */
    @Override
    public Pair<LoginResponse, String> checkLogin(String userName, String password) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setString(1, userName);
            ResultSet table  = stmt.executeQuery();
            if (!table.next()) {
                return new Pair<>(LoginResponse.UserDoesNotExist, null);
            } else {
                if (table.getString("password") == null && table.getString("token") == null) {
                    return new Pair<>(LoginResponse.AccountBlocked, null);
                } else {
                    if (table.getString("password") == null) {
                        return new Pair<>(LoginResponse.PasswordAlreadyUsed, null);
                    } else if (!table.getString("password").equals(password)) {
                        return new Pair<>(LoginResponse.WrongPassword, null);
                    } else {
                        return new Pair<>(LoginResponse.Valid, table.getString("token"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("An exception occurred during the check of the login data.");
            System.err.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
    }

    /**
     * This method checks whether a given token is a valid token.
     *
     * @param token The token to be checked.
     * @return A {@link TokenResponse} indicating whether the token is valid, was already blocked or whether the token
     * corresponds to an admin or not.
     */
    @Override
    public TokenResponse checkToken(String token) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM users WHERE token = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
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
            System.err.println("An exception occurred during a token check.");
            System.err.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
    }

    /**
     * Converts a token to a user ID.
     *
     * @param token The token of the user.
     * @return the ID of the user with the given token.
     *
     * @throws IllegalArgumentException if the token does not exist.
     */
    @Override
    public int tokenToID(String token) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM users WHERE token  = ?";
        int ID = -1;
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setString(1, token);
            ResultSet doc  = stmt.executeQuery();
            if (doc.next()) {
                return doc.getInt("userID");
            } else {
                throw new IllegalArgumentException("Token not found in database");
            }
        } catch (SQLException ex) {
            System.err.println("An exception occurred while converting IDs to tokens.");
            System.err.println(ex.getMessage());
        } finally {
            this.closeConnection();
        }
        return ID;
    }

    /**
     * This methods deletes a user entry in the database.
     *
     * @param userID The ID of the user to be deleted.
     * @return True, iff the user was successfully removed.
     */
    @Override
    public boolean removeUser(int userID) {
        this.openConnection();
        String sqlstatement = "DELETE FROM users WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setInt(1, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("An exception occurred while removing a user from the database.");
            System.err.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    /**
     * This method logs out a user in the database. This method must not delete the entry but should indicate that the
     * user cannot log in again.
     *
     * @param userID The ID of the user to be deleted.
     * @return True, iff the operation was successful.
     */
    @Override
    public boolean logoutUser(int userID) {
        this.openConnection();
        String sqlstatement = "UPDATE users SET password = ? , token = ?  WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setNull(1, java.sql.Types.VARCHAR);
            stmt.setNull(2, java.sql.Types.VARCHAR);
            stmt.setInt(3, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("An exception occurred while logging out/invalidating a user.");
            System.err.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    /**
     * Returns a list of all passwords to be handed out to the users, combined with their ID.
     *
     * @return A list pairing {@link User}-objects with their passwords.
     */
    @Override
    public List<Pair<User, String>> getAllPasswords() {
        this.openConnection();
        List<Pair<User, String>> pass = new LinkedList<>();
        String sqlstatement = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement);
             ResultSet att  = stmt.executeQuery()) {
            while (att.next()) {
                User user = new Attendee(att.getString("fullname"),
                        att.getString("email"),
                        att.getString("username"),
                        att.getString("groups"),
                        att.getString("residence"),
                        att.getString("function"),
                        att.getInt("userID"));
                pass.add(new Pair<>(user, att.getString("password")));
            }
        } catch (SQLException ex) {
            System.err.println("An exception occurred while reading creating a password list.");
            System.err.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return pass;
    }

    /**
     * Overwrites the token of the user, in case there was a problem with the device.
     *
     * @param userID The ID of the user.
     * @param token  The new token.
     * @return True, iff the new token was successfully added to the database.
     */
    @Override
    public boolean storeNewToken(int userID, String token) {
        this.openConnection();
        String sqlstatement = "UPDATE users SET token = ? WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setString(1, token);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("An exception occurred while storing a new user token.");
            System.err.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    /**
     * Overwrites the password of the user, in case there was a problem with the device.
     *
     * @param userID   The ID of the user.
     * @param password The new password.
     * @return True, iff the new password was successfully added to the database.
     */
    @Override
    public boolean storeNewPassword(int userID, String password) {
        this.openConnection();
        String sqlstatement = "UPDATE users SET password = ?  WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setString(1, password);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("An exception occurred while storing a new user password.");
            System.err.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    /**
     * This methods checks whether a username was already used to enable unique username creation.
     *
     * @param userName The username that should be checked.
     * @return True, iff the username was already in the database.
     */
    @Override
    public boolean userNameAlreadyUsed(String userName) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setString(1, userName);
            try (ResultSet table  = stmt.executeQuery()) {
                return !table.isAfterLast();
            }

        } catch (SQLException ex) {
            System.err.println("An exception occurred while checking whether a username was already used.");
            System.err.println(ex.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
    }

    /**
     * @return a list of all IDs.
     */
    @Override
    public List<Integer> getIDs() {
        this.openConnection();
        List<Integer> IDs = new LinkedList<>();
        String sqlstatement = "SELECT userID FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement);
             ResultSet table  = stmt.executeQuery()) {
            while (table.next()) {
                IDs.add(table.getInt("userID"));
            }
        } catch (SQLException ex) {
            System.err.println("An exception occurred while reading all user IDs in the database.");
            System.err.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return IDs;
    }

    /**
     * Adds a new {@link Attendee} to the database that is not an admin.
     *
     * @param a        The new {@link Attendee}.
     * @param password The password of the attendee.
     * @param token    The token of the attendee.
     * @return True, iff the attendee was added correctly.
     */
    @Override
    public boolean addAttendee(Attendee a, String password, String token) {
        this.openConnection();
        String sqlstatement = "INSERT INTO users(userID, fullname, username, password, "
                + "token, email, groups, function, residence, isAdmin, present)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setInt(1, a.getID());
            stmt.setString(2, a.getName());
            stmt.setString(3, a.getUserName());
            stmt.setString(4, password);
            stmt.setString(5, token);
            stmt.setString(6, a.getEmail());
            stmt.setString(7, a.getGroup());
            stmt.setString(8, a.getFunction());
            stmt.setString(9, a.getResidence());
            stmt.setBoolean(10, false);
            stmt.setBoolean(11, false);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("An exception occurred while adding a new attendee.");
            System.err.println(ex.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    /**
     * @return a list of all {@link Attendee}s in the database.
     */
    @Override
    public List<Attendee> getAllAttendees() {
        this.openConnection();
        List<Attendee> attendees = new LinkedList<>();
        String sqlstatement = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement);
             ResultSet table  = stmt.executeQuery()) {
            while (table.next()) {
                attendees.add(new Attendee(table.getString("fullname"),
                        table.getString("email"),
                        table.getString("username"),
                        table.getString("groups"),
                        table.getString("residence"),
                        table.getString("function"),
                        table.getInt("userID")));
            }
        } catch (SQLException ex) {
            System.err.println("An exception occurred while reading all attendees.");
            System.err.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return attendees;
    }

    /**
     * Returns the {@link Attendee} with the given userID.
     *
     * @param userID The ID of the user.
     * @return the {@link Attendee} object.
     */
    @Override
    public Attendee getAttendeeData(int userID) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM users  WHERE userID = ?";
        Attendee attendee = null;
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setInt(1, userID);
            ResultSet att  = stmt.executeQuery();
            attendee = new Attendee(att.getString("fullname"),
                    att.getString("email"),
                    att.getString("username"),
                    att.getString("groups"),
                    att.getString("residence"),
                    att.getString("function"),
                    att.getInt("userID"));
        } catch (SQLException ex) {
            System.err.println("An exception occurred while trying to return an attendee.");
            System.err.println(ex.getMessage());
        } finally {
            this.closeConnection();
        }
        return attendee;
    }

    /**
     * Overwrite the {@link Attendee} in the database.
     *
     * @param a The new {@link Attendee} object.
     * @return True, iff the Attendee was overwritten properly
     */
    @Override
    public boolean editAttendee(Attendee a) {
        this.openConnection();
        String sqlstatement = "UPDATE users SET fullname = ?, "
                + " email = ?, "
                + " username = ? , "
                + " groups = ? , "
                + " residence = ? , "
                + " function = ? "
                + " WHERE userID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getEmail());
            stmt.setString(3, a.getUserName());
            stmt.setString(4, a.getGroup());
            stmt.setString(5, a.getResidence());
            stmt.setString(6, a.getFunction());
            stmt.setInt(7, a.getID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("An exception occurred while trying to overwrite an attendee.");
            System.err.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    /**
     * Adds a new {@link Admin} to the database.
     *
     * @param a        The new {@link Admin}.
     * @param password The password of the admin.
     * @param token    The token of the admin.
     * @return True, iff the admin was added correctly.
     */
    @Override
    public boolean addAdmin(Admin a, String password, String token) {
        this.openConnection();
        String sqlstatement = "INSERT INTO users(userID, fullname, username, password ,"
                + "token, email, groups, function, residence, isAdmin, present)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setInt(1, a.getID());
            stmt.setString(2, a.getName());
            stmt.setString(3, a.getUserName());
            stmt.setString(4, password);
            stmt.setString(5, token);
            stmt.setString(6, a.getEmail());
            stmt.setString(7, a.getGroup());
            stmt.setString(8, a.getFunction());
            stmt.setString(9, a.getResidence());
            stmt.setBoolean(10, true);
            stmt.setBoolean(11, false);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("An exception occurred while adding a new admin.");
            System.err.println(ex.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }

    /**
     * @return a list of all {@link Admin}s in the database.
     */
    @Override
    public List<Admin> getAllAdmins() {
        this.openConnection();
        List<Admin> admins = new LinkedList<>();
        String sqlstatement = "SELECT * FROM users WHERE isAdmin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setBoolean(1, true);
            try (ResultSet table  = stmt.executeQuery()) {
                while (table.next()) {
                    admins.add(new Admin(table.getString("fullname"),
                            table.getString("email"),
                            table.getString("username"),
                            table.getString("groups"),
                            table.getString("residence"),
                            table.getString("function"),
                            table.getInt("userID")));
                }
            }
        } catch (SQLException ex) {
            System.err.println("An exception occurred while reading all admins.");
            System.err.println(ex.getMessage());
            return null;
        } finally {
            this.closeConnection();
        }
        return admins;
    }

    /**
     * Returns the {@link Admin} with the given userID.
     *
     * @param userID The ID of the admin.
     * @return the {@link Admin} object.
     */
    @Override
    public Admin getAdminData(int userID) {
        this.openConnection();
        String sqlstatement = "SELECT * FROM users WHERE userID = ? AND isAdmin = ?";
        Admin admin = null;
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement)) {
            stmt.setInt(1, userID);
            stmt.setBoolean(2, true);
            try (ResultSet adm  = stmt.executeQuery()) {
                admin = new Admin(adm.getString("fullname"),
                        adm.getString("email"),
                        adm.getString("username"),
                        adm.getString("groups"),
                        adm.getString("residence"),
                        adm.getString("function"),
                        adm.getInt("userID"));
            }
        } catch (SQLException ex) {
            System.err.println("An exception occurred while trying to return an admin.");
            System.err.println(ex.getMessage());
        } finally {
            this.closeConnection();
        }
        return admin;
    }

    /**
     * Overwrite the {@link Admin} in the database.
     *
     * @param a The new {@link Admin} object.
     * @return True, iff the Admin was overwritten properly
     */
    @Override
    public boolean editAdmin(Admin a) {
        this.openConnection();
        String sqlstatement = "UPDATE users SET fullname = ? , "
                + "email = ? ,"
                + "groups = ? , "
                + "residence = ? , "
                + "function = ?"
                + " WHERE userID = ? AND isAdmin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sqlstatement);) {
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getEmail());
            stmt.setString(3, a.getGroup());
            stmt.setString(4, a.getResidence());
            stmt.setString(5, a.getFunction());
            stmt.setInt(6, a.getID());
            stmt.setBoolean(7, true);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("An exception occurred while trying to overwrite an admin.");
            System.err.println(e.getMessage());
            return false;
        } finally {
            this.closeConnection();
        }
        return true;
    }
}
