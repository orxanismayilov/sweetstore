package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import sample.utils.ScreenUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    private Properties properties;
    private final static String PROPERTIES_URL= "sample/resource/properties/fxmlurls.properties";

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    public void loginButtonAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event, properties.getProperty("homepage"));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText("Please fill out this field.");
        txtUsername.setTooltip(tooltip);
        txtPassword.setTooltip(tooltip);
        loadPropertiesFile();
    }

    private void loadPropertiesFile() {
        try {
            InputStream input = LoginController.class.getClassLoader().getResourceAsStream(PROPERTIES_URL);
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}