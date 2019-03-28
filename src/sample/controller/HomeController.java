package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.utils.ScreenUtils;

import java.io.IOException;

public class HomeController {

    private final static String FXML_URL_ORDERPAGE="../resource/screens/orderpage.fxml";
    private final static String FXML_URL_STOCKPAGE="../resource/screens/stockpage.fxml";
    private final static String FXML_URL_LOGINPAGE="/sample/resource/screens/loginpage.fxml";

    @FXML private BorderPane pane;

    public void btnSalesAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event,FXML_URL_ORDERPAGE);
    }

    public void btnStockAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event,FXML_URL_STOCKPAGE);
    }

    public void buttonLogOutAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_LOGINPAGE));
        Parent root = loader.load();
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
