import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main extends Application {

    private Properties fxmlProperties;
    private Properties applicationProperties;
    private static String FXML_PROPERTIES_URL="C:\\Users\\Orxan\\Desktop\\Home Project\\Home Project\\src\\sample\\resource\\properties\\fxmlurls.properties";
    private static String APP_PROPERTIES_URL="C:\\Users\\Orxan\\Desktop\\Home Project\\Home Project\\src\\sample\\resource\\properties\\application.properties";

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            InputStream fxmlInputStream= new FileInputStream(FXML_PROPERTIES_URL);
            InputStream appInputStream= new FileInputStream(APP_PROPERTIES_URL);
            fxmlProperties=new Properties();
            applicationProperties =new Properties();
            applicationProperties.load(appInputStream);
            fxmlProperties.load(fxmlInputStream);
        }catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(getClass().getResource(fxmlProperties.getProperty("loginpage")));
        primaryStage.setTitle(applicationProperties.getProperty("maintitle"));
        primaryStage.setScene(new Scene(root, Double.valueOf(applicationProperties.getProperty("mainwidth")),Double.valueOf(applicationProperties.getProperty("mainheigth"))));
        primaryStage.setMinWidth(1300);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
