package org.bharath.dao;

import org.bharath.model.Users;
import java.util.List;

public interface UsersDao {

    List<Users> listAllUsers();
    List<Users> listAllUserRoleAccounts();
    List<Users> listAllTheAdminAccounts();
    boolean resetOwnPassword(int targetUser, String password);
    boolean removeUser(int userId, int targetUser);
    boolean isAdminCheck(int userId);
    boolean isUserRoleCheck(int userId);
    int addNewUser(Users users);
    boolean updateTheRoleOfTheUser(int userId, int targetUserId, String value);
    Users getUser(int userId);
    boolean blockAndUnblock(int userId, int targetUser, char value);
    boolean isUserAccountBlocked(int userId);
    boolean isUserIdExits(int userId);
    boolean isEmailAlreadyExits(String emailId);
    int getUserIdByEmailId(String emailId);
    boolean resetPassword(int userId, int targetUser, String password);
}
