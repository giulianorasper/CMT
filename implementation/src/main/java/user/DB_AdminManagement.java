package user;

import java.util.List;

@SuppressWarnings("checkstyle:typename")
public interface DB_AdminManagement {

    boolean addAdmin(Admin a, String password, String token);

    List<Admin> getAllAdmins();

    Admin getAdminData(String adminToken);

    boolean editAdmin(Admin a);
}
