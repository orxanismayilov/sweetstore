package repository.impl;

import sample.enums.UserRole;
import sample.model.User;
import sample.model.UserSession;
import sample.repository.UserDao;
import sample.utils.DBConnection;
import sample.utils.PasswordAuthentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    private UserSession userSession;

    @Override
    public void startUserSesion(User user) {
        userSession=UserSession.getInstance();
        userSession.setUser(user);
    }


    @Override
    public void addUserAddList(User user) {
        String sql = "INSERT into USERS (Name,password,role,is_active) values (?,?,?,?)";
        PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
        String password = passwordAuthentication.hash(user.getPassword());
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             ps.setString(1, user.getName());
             ps.setString(2, password);
             ps.setString(3, user.getRole().toString());
             ps.setInt(4, 1);
             ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUserById(int id) {
        
    }

    @Override
    public boolean validateLogin(User user) {
        String sql="SELECT * from USERS where CAST(name AS BINARY) = ?  and is_active=1 ";
        String password=user.getPassword();
        try (Connection con=DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)) {
             ps.setString(1,user.getName());
            try (ResultSet rs1=ps.executeQuery()){

                    while (rs1.next()) {
                        User nUser=new User();
                        nUser.setName(rs1.getString("name"));
                        nUser.setPassword(rs1.getString("password"));
                        nUser.setId(rs1.getInt("id"));
                        nUser.setRole(UserRole.valueOf(rs1.getString("role")));
                        return authcateUserPassword(nUser,password);
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean authcateUserPassword(User user, String password) {
        PasswordAuthentication passwordAuthentication=new PasswordAuthentication();
        if(passwordAuthentication.authenticate(password,user.getPassword())) {
            startUserSesion(user);
            return true;
        }
        return false;
    }
}
