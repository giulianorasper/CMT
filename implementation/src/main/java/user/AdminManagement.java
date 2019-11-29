package user;

import utils.OperationResponse;
import utils.Pair;

import java.util.List;

public interface AdminManagement {

    OperationResponse addAdmin(Admin a);

    List<Admin> getAllAdmins();

    Admin getAdminPersonalData(int ID);

    OperationResponse removeAdmin(int ID);

    OperationResponse logoutAdmin(int ID);

    OperationResponse editAdmin(int ID, Admin a);

    OperationResponse generateNewPassword(int ID);

    OperationResponse generateNewToken(int ID);
}
