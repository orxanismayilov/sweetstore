package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import sample.model.User;
import sample.service.UserService;
import sample.utils.LoadPropertyUtil;
import sample.utils.ScreenUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    private Properties properties;
    private final static String PROPERTIES_URL= "C:\\Users\\Orxan\\Desktop\\HomeProject\\src\\sample\\resource\\properties\\fxmlurls.properties";
    private UserService userService;

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    public void loginButtonAction(ActionEvent event) throws IOException {
        User user=new User();
        user.setName(txtUsername.getText());
        user.setPassword(txtPassword.getText());
        if (userService.validateLogin(user)) {
            ScreenUtils.changeScreen(event, properties.getProperty("homepage"));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService=new UserService();
        final Tooltip tooltip = new Tooltip();
        tooltip.setText("Please fill out this field.");
        txtUsername.setTooltip(tooltip);
        txtPassword.setTooltip(tooltip);
        properties= LoadPropertyUtil.loadPropertiesFile(PROPERTIES_URL);
    }
}