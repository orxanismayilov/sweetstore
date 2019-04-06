package sample.controller;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Product;
import sample.service.ProductService;
import sample.utils.Notification;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    private Product product;
    private Stage stage;
    private ProductService productService;
    private Notification notification;
    private final static String NAME_ERROR="Please enter valid name.";
    private final static String PRICE_ERROR="Price must be positive and less then 1000.";
    private final static String QUANTITY_ERROR="Quantity must be positive and less then 1000.";
    private final static String PRICE_REGEX="^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$";
    private final static String QUANTITY_REGEX="\\d*";
    private final static String NAME_REGEX="(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$";
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");
    @FXML private TextField productName;
    @FXML private TextField productQuantity;
    @FXML private TextField productPrice;
    @FXML private Button buttonSave;
    @FXML private Button buttonCancel;
    @FXML private Label lblAlert;


    public void saveProduct(){

        try {
            product=new Product(productName.getText(), Integer.parseInt(productQuantity.getText()), new BigDecimal(productPrice.getText()), LocalDate.now());
        } catch (NumberFormatException e) {
            String ALERT_TEXT = "Please enter valid input!";
            lblAlert.setText(ALERT_TEXT);
            return;
           }
        notification=productService.addData(product);
        if(!notification.hasError()){
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            stage.close();
        } else {
            String errors=notification.errorMessage();
            lblAlert.setText(errors);
            handleErrors();
        }
    }

    public void cancelAction(){
        Stage stage=(Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    public void setProductService(ProductService productService){
        this.productService=productService;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validatePrice();
        validateQuantity();
        validateName();
    }

    private void validateName(){
        productName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches(NAME_REGEX)){
               productName.pseudoClassStateChanged(errorClass,true);
            }else {
                productName.pseudoClassStateChanged(errorClass,false);
            }
        });
    }

    private void validatePrice () {
        productPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(PRICE_REGEX)) {
                productPrice.pseudoClassStateChanged(errorClass, true);
            } else {
                productPrice.pseudoClassStateChanged(errorClass, false);
            }
        });
    }

    private  void validateQuantity() {
        productQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(QUANTITY_REGEX)) {
                productQuantity.pseudoClassStateChanged(errorClass, true);
            } else {
                productQuantity.pseudoClassStateChanged(errorClass, false);
            }
        });
    }

    private void handleErrors(){
        Map<String,Boolean> map=productService.getValidation();
        if(map.get("nameError")){
            productName.pseudoClassStateChanged(errorClass,true);
        } else {
            productName.pseudoClassStateChanged(errorClass,false);
        }
        if(map.get("quantityError")){
            productQuantity.pseudoClassStateChanged(errorClass,true);
        }else {
            productQuantity.pseudoClassStateChanged(errorClass,false);
        }
        if(map.get("priceError")) {
            productPrice.pseudoClassStateChanged(errorClass,true);
        }else {
            productPrice.pseudoClassStateChanged(errorClass,false);
        }
    }
}

