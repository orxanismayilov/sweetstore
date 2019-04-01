package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
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
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    private Product product;
    Notification notification;
    private static String ALERT_TEXT="Please enter valid input!";
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


    public void saveProduct(ActionEvent event){

        try {
            product=new Product(productName.getText(), Integer.parseInt(productQuantity.getText()), BigDecimal.valueOf(Double.parseDouble(productPrice.getText())), LocalDate.now());
        } catch (NumberFormatException e) {
            lblAlert.setText(ALERT_TEXT);
            return;
           }
        notification=productService.addData(product);
        if(!notification.hasError()){
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            stage.close();
        } else {
            String errors=notification.errorMessage();
            handleErrors(errors);
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
        validatePrice();
        validateQuantity();
        validateName();
    }

    private void validateName(){
        productName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches(NAME_REGEX)){
                   productName.pseudoClassStateChanged(errorClass,true);
                   lblAlert.setText(NAME_ERROR);
                }else {
                    productName.pseudoClassStateChanged(errorClass,false);
                    lblAlert.setText("");
                }
            }
        });
    }

    private void validatePrice () {
        productPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(PRICE_REGEX)) {
                    productPrice.pseudoClassStateChanged(errorClass, true);
                    lblAlert.setText(PRICE_ERROR);
                } else {
                    productPrice.pseudoClassStateChanged(errorClass, false);
                    lblAlert.setText("");
                }
            }
        });
    }

    private  void validateQuantity() {
        productQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches(QUANTITY_REGEX)) {
                    productQuantity.pseudoClassStateChanged(errorClass, true);
                    lblAlert.setText(QUANTITY_ERROR);
                } else {
                    productQuantity.pseudoClassStateChanged(errorClass, false);
                    lblAlert.setText("");
                }
            }
        });
    }

    private void handleErrors(String errors){
        if(errors.contains(NAME_ERROR)){
            lblAlert.setText(errors);
            productName.pseudoClassStateChanged(errorClass,true);
        } else {
            productName.pseudoClassStateChanged(errorClass,false);
        }
        if(errors.contains(QUANTITY_ERROR)){
            lblAlert.setText(errors);
            productQuantity.pseudoClassStateChanged(errorClass,true);
        }else {
            productQuantity.pseudoClassStateChanged(errorClass,false);
        }
        if(errors.contains(PRICE_ERROR)) {
            lblAlert.setText(errors);
            productPrice.pseudoClassStateChanged(errorClass,true);
        }else {
            productPrice.pseudoClassStateChanged(errorClass,false);
        }
    }
}

