package repository;

import sample.model.User;

public interface UserDao {
    void startUserSesion(User user);

    void addUserAddList(User user);

    void deleteUserById(int id);

    boolean validateLogin(User user);
}
