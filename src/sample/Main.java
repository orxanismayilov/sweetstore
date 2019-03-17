package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("screens/loginpage.fxml"));
        primaryStage.setTitle("Sweet Store");
        primaryStage.setScene(new Scene(root, 1100, 550));
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(550);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
