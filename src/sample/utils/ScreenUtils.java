package sample.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ScreenUtils {


    public static void changeScreen(ActionEvent event, String fxmlFile) throws IOException {
        Parent newPage = FXMLLoader.load(ScreenUtils.class.getResource(fxmlFile));
        ((Node) event.getSource()).getScene().setRoot(newPage);
    }

}
