package sample.controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.model.Product;
import sample.service.ProductService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    private Product product;
    private boolean validation;
    private static int increment=4;
    @FXML private TextField productName;
    @FXML private TextField productQuantity;
    @FXML private TextField productPrice;
    @FXML private Button buttonSave;
    @FXML private Button buttonCancel;
    @FXML private Label lblAlert;


    public void saveProduct(ActionEvent event) throws IOException {

        try {
            product=new Product(increment, productName.getText(), Integer.parseInt(productQuantity.getText()), Double.parseDouble(productPrice.getText()), LocalDate.now());
        } catch (NumberFormatException e) {
            filledAlert();
            return;
           }
        validation=productService.addData(product);
        if(validation){
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            stage.close();
        } else {
            filledAlert();
        }
    }

    public void cancelAction(ActionEvent event){
        Stage stage=(Stage) buttonCancel.getScene().getWindow();
        stage.close();

    }

    private ProductService productService;
    public void setProductService(ProductService productService){
        this.productService=productService;
    }

    private void filledAlert(){
        lblAlert.setText("Please enter valid input!");
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(3)
        );
        visiblePause.setOnFinished(
                event2 -> lblAlert.setVisible(false)
        );
        visiblePause.play();
    }

    private Stage stage;
    public void setStage(Stage stage){
        this.stage = stage;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
