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

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateProductController implements Initializable {

    private static String ALERT_TEXT="Please enter valid input!";
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");
    private Stage stage;
    private Product product;
    private ProductService productService;

    @FXML private TextField fieldName;
    @FXML private Label labelQuantity;
    @FXML private TextField fieldPrice;
    @FXML private Button buttonSave,buttonCancel;
    @FXML private Label labelAlert;

    public void buttonSaveAction(){
        product.setName(fieldName.getText());
        product.setPrice(BigDecimal.valueOf(Double.parseDouble(fieldPrice.getText())));
        boolean validation=productService.updateProductNameandPrice(product);
        if(validation) {
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            stage.close();
        } else labelAlert.setText(ALERT_TEXT);
    }

    public void buttonCancelAction(){
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    public void setFileds(){
        fieldName.setText(product.getName());
        labelQuantity.setText(String.valueOf(product.getQuantity()));
        fieldPrice.setText(String.valueOf(product.getPrice()));
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fieldPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$")) {
                    fieldPrice.pseudoClassStateChanged(errorClass,true);
                    labelAlert.setText(ALERT_TEXT);
                } else {
                    fieldPrice.pseudoClassStateChanged(errorClass,false);
                    labelAlert.setText("");
                }
            }
        });
    }

    public void setProductService(ProductService productService) {
        this.productService=productService;
    }
}
