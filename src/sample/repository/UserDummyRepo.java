package sample.repository;

import sample.enums.UserRole;
import sample.model.User;
import sample.model.UserSession;

import java.util.ArrayList;
import java.util.List;

public class UserDummyRepo {
    private List<User> userList;
    private UserSession userSession;

    public UserDummyRepo() {
        userSession=UserSession.getInstance();
        User user = new User();
        userList = new ArrayList<>();
        user.setId(1);
        user.setName("orxan");
        user.setPassword("orxan123");
        user.setRole(UserRole.ADMIN);
        userList.add(user);
    }

    public void addUserAddList(User user) {
        userList.add(user);
    }

    public void deleteUserById(int id) {
        for (User user : userList) {
            if (user.getId() == id) {
                userList.remove(user);
            }
        }
    }

    public boolean validateLogin(User user) {
        for (User u : userList) {
            if (u.getName().equals(user.getName()) && u.getPassword().equals(user.getPassword())) {
                userSession.setUserRole(u.getRole());
                return true;
            }
        }
        return false;
    }

}
