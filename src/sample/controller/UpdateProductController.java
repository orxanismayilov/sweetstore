package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdateProductController implements Initializable {

    private static String ALERT_TEXT="Please enter valid input!";
    private static String PRICE_REGEX="^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$";
    private static String NAME_ERROR_KEY="nameError";
    private static String PRICE_ERROR_KEY="priceError";
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");
    private Stage stage;
    private Product existedProduct;
    private ProductService productService;

    @FXML private TextField fieldName;
    @FXML private Label labelQuantity;
    @FXML private TextField fieldPrice;
    @FXML private Button buttonSave,buttonCancel;
    @FXML private Label labelAlert;

    public void buttonSaveAction(){
        Product product=new Product();
        buttonSaveCreation(product);

    }

    private void buttonSaveCreation(Product product){
        product.setName(fieldName.getText());
        product.setQuantity(existedProduct.getQuantity());
        product.setPrice(BigDecimal.valueOf(Double.parseDouble(fieldPrice.getText())));
        Notification error=productService.isProductValid(product);
        if(!error.hasError()) {
            existedProduct.setName(product.getName());
            existedProduct.setPrice(product.getPrice());
            existedProduct.setUpdateDate(LocalDateTime.now());
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            stage.close();
        } else {
            labelAlert.setText(error.errorMessage());
            handleErrors();
        }
    }

    public void buttonCancelAction(){
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    public void setFields(){
        fieldName.setText(existedProduct.getName());
        labelQuantity.setText(String.valueOf(existedProduct.getQuantity()));
        fieldPrice.setText(String.valueOf(existedProduct.getPrice()));
    }

    public void setProduct(Product product) {
        this.existedProduct = product;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validatePrice();
    }

    private void validatePrice(){
        fieldPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches(PRICE_REGEX)) {
                    fieldPrice.pseudoClassStateChanged(errorClass,true);
                } else {
                    fieldPrice.pseudoClassStateChanged(errorClass,false);
                }
            }
        });
    }

    private void handleErrors(){
        Map<String,Boolean> map=productService.getValidation();
        if (map.get(NAME_ERROR_KEY)){
            fieldName.pseudoClassStateChanged(errorClass,true);
        } else {
            fieldName.pseudoClassStateChanged(errorClass,false);
        }

        if (map.get(PRICE_ERROR_KEY)){
            fieldPrice.pseudoClassStateChanged(errorClass,true);
        }else {
            fieldPrice.pseudoClassStateChanged(errorClass,false);
        }
    }

    public void setProductService(ProductService productService) {
        this.productService=productService;
    }

}
