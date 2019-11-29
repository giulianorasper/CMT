package user;

import java.util.List;

public interface DB_AdminManagement {

    boolean addAdmin(Admin a, String password);

    List<Admin> getAllAdmins();

    Admin getAdminData(String adminToken);

    boolean editAdmin(Admin a);
}
