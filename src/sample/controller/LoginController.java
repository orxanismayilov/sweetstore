package sample.controller;

import javafx.event.ActionEvent;
import sample.utils.ScreenUtils;

import java.io.IOException;

public class LoginController {
    private final static String FXML_URL="../screens/homepage.fxml";

    public void loginButtonAction(ActionEvent event)throws IOException {
        ScreenUtils.changeScreen(event,FXML_URL);
    }
}
