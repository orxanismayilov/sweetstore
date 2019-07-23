package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import model.User;
import repository.impl.UserDaoImpl;
import service.UserService;
import service.serviceImpl.UserServiceImpl;
import utils.LoadPropertyUtil;
import utils.ScreenUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    private Properties properties;
    private final static String PROPERTIES_URL= "sample/resource/properties/fxmlurls.properties";
    private UserService userService;

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label labelAlert;

    public void loginButtonAction(ActionEvent event) throws IOException {
        User user=new User();
        user.setName(txtUsername.getText());
        user.setPassword(txtPassword.getText());
        if (userService.validateLogin(user)) {
            ScreenUtils.changeScreen(event, properties.getProperty("homepage"));
        } else {
            labelAlert.setText("Username or password is wrong.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService=new UserServiceImpl(new UserDaoImpl());
        final Tooltip tooltip = new Tooltip();
        tooltip.setText("Please fill out this field.");
        txtUsername.setTooltip(tooltip);
        txtPassword.setTooltip(tooltip);
        properties= LoadPropertyUtil.loadPropertiesFile(PROPERTIES_URL);
    }
}