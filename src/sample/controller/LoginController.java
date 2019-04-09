package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import sample.utils.ScreenUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class LoginController implements Initializable {

    private final static String FXML_URL="../resource/screens/homepage.fxml";

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    public void loginButtonAction(ActionEvent event)throws IOException {
        ScreenUtils.changeScreen(event,FXML_URL);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText("Please fill out this field.");
        txtUsername.setTooltip(tooltip);
        txtPassword.setTooltip(tooltip);
    }
}
