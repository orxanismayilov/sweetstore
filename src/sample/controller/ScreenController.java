package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenController  {

   public void changeScreen(ActionEvent event, String fxmlFile) throws IOException {
        Parent newPage = FXMLLoader.load(getClass().getResource(fxmlFile));
        ((Node) event.getSource()).getScene().setRoot(newPage);
    }
  /*  public void newScreen(String fxmlFile) throws IOException {
       Parent newPage=FXMLLoader.load(getClass().getResource(fxmlFile));
       Stage stage=new Stage();
       stage.setScene(new Scene(newPage,700,400));
       stage.setTitle("Update");
       stage.show();
    }*/
}
