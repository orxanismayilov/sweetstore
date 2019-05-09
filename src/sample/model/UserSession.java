package sample.model;

import sample.enums.UserRole;

public class UserSession {
    private static UserSession instance=null;
    private UserRole userRole;
    private String userName;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance==null){
           instance =new UserSession();
        }
        return instance;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public static void setInstance(UserSession instance) {
        UserSession.instance = instance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
