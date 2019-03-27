package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Product;
import sample.service.ProductService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    private Product product;
    private boolean validation;
    private static int increment=4;
    private static String ALERT_TEXT="Please enter valid input!";
    private static String TEXTFIELD_ERROR_STYLESHEET="-fx-text-box-border: red ;" + "-fx-focus-color: red ;"+ " -fx-border-radius: 3px;";
    private static String TEXTFIELD_DEFAULT_STYLE="-fx-focus-color:rgba(3, 158, 211);";

    @FXML private TextField productName;
    @FXML private TextField productQuantity;
    @FXML private TextField productPrice;
    @FXML private Button buttonSave;
    @FXML private Button buttonCancel;
    @FXML private Label lblAlert;


    public void saveProduct(ActionEvent event){

        try {
            product=new Product(increment, productName.getText(), Integer.parseInt(productQuantity.getText()), Double.parseDouble(productPrice.getText()), LocalDate.now());
        } catch (NumberFormatException e) {
            lblAlert.setText(ALERT_TEXT);
            return;
           }
        validation=productService.addData(product);
        if(validation){
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            stage.close();
        } else {
            lblAlert.setText(ALERT_TEXT);
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

    private Stage stage;
    public void setStage(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    productQuantity.setStyle(TEXTFIELD_ERROR_STYLESHEET);
                    lblAlert.setText(ALERT_TEXT);
                } else {
                    productQuantity.setStyle(TEXTFIELD_DEFAULT_STYLE);
                    lblAlert.setText("");
                }
            }
        });

        productPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$")) {
                    productPrice.setStyle(TEXTFIELD_ERROR_STYLESHEET);
                    lblAlert.setText(ALERT_TEXT);
                } else {
                    productPrice.setStyle(TEXTFIELD_DEFAULT_STYLE);
                    lblAlert.setText("");
                }
            }
        });
    }
}
