package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import sample.utils.LoadPropertyUtil;
import sample.utils.ScreenUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private Properties properties;
    private final static String PROPERTIES_URL= "C:\\Users\\Orxan\\Desktop\\Home Project\\Home Project\\src\\sample\\resource\\properties\\fxmlurls.properties";

    @FXML private BorderPane pane;

    public void btnSalesAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event,properties.getProperty("orderpage"));
    }

    public void btnStockAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event,properties.getProperty("stockpage"));
    }

    public void buttonLogOutAction() throws IOException {
        FXMLLoader loader =new FXMLLoader(getClass().getResource(properties.getProperty("loginpage")));
        Parent root = loader.load();
        pane.getScene().setRoot(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        properties= LoadPropertyUtil.loadPropertiesFile(PROPERTIES_URL);
    }
}
