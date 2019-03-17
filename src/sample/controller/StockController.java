package sample.controller;

import javafx.event.ActionEvent;

import java.io.IOException;

public class StockController {

    private ScreenController screenController;

    public void btnBackAction(ActionEvent event) throws IOException {
        screenController = new ScreenController();
        screenController.changeScreen(event, "../screens/homepage.fxml");
    }

}
