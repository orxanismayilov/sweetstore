package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.LoadPropertyUtil;

import java.util.Properties;

public class Main extends Application {
    private Properties fxmlProperties;
    private Properties applicationProperties;
    private static String FXML_PROPERTIES_URL= "/java/resources/properties/fxmlurls.properties";
    private static String APP_PROPERTIES_URL="resources/properties/application.properties";

    @Override
    public void start(Stage primaryStage) throws Exception{
//        System.out.println(new app.Main().getClass().getClassLoader().getResource("HomeProject/src/main/java/resources/properties/fxml.properties").getPath());
        fxmlProperties= LoadPropertyUtil.loadPropertiesFile(FXML_PROPERTIES_URL);
        applicationProperties =LoadPropertyUtil.loadPropertiesFile(APP_PROPERTIES_URL);
        Parent root = FXMLLoader.load(getClass().getResource("/resources/screens/loginpage.fxml"));
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
