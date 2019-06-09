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
import sample.service.serviceImpl.ProductServiceImpl;
import sample.utils.NumberUtils;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdateProductController implements Initializable {

    private final static String PRICE_ERROR = "Please enter valid price.";
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");
    private Product existedProduct;
    private ProductService productService;

    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldQuantity;
    @FXML
    private TextField fieldPrice;
    @FXML
    private Button buttonSave, buttonCancel;
    @FXML
    private Label labelAlert;

    public void buttonSaveAction() {
        Product product = new Product();
        product.setName(fieldName.getText());
        product.setQuantity(existedProduct.getQuantity());
        try {
            product.setPrice(Float.parseFloat(fieldPrice.getText()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            labelAlert.setText(PRICE_ERROR);
            return;
        }

        Map<String, Map<Boolean, List<String>>> validation = productService.updateProduct(product, existedProduct.getId());

        if (!validation.get("nameError").containsKey(true) && !validation.get("quantityError").containsKey(true) && !validation.get("priceError").containsKey(true)) {
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            stage.close();
        } else {

            handleErrors(validation);
        }

    }


    public void buttonCancelAction() {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    void setFields(Product product) {
        this.existedProduct = product;
        fieldName.setText(existedProduct.getName());
        fieldQuantity.setText(String.valueOf(existedProduct.getQuantity()));
        fieldPrice.setText(String.valueOf(existedProduct.getPrice()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validatePrice();
    }

    private void validatePrice() {
        fieldPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (NumberUtils.isNumberFloat(newValue)) {
                fieldPrice.pseudoClassStateChanged(errorClass, false);
            } else {
                fieldPrice.pseudoClassStateChanged(errorClass, true);
            }
        });
    }

    private void handleErrors(Map<String, Map<Boolean, List<String>>> validation) {
        Map<Boolean, List<String>> nameMap = validation.get("nameError");
        Map<Boolean, List<String>> priceMap = validation.get("priceError");
        StringBuilder errors = new StringBuilder();
        if (nameMap.containsKey(true)) {
            fieldName.pseudoClassStateChanged(errorClass, true);
            List<String> ls = nameMap.get(true);
            for (String error : ls) {
                errors.append(error).append("\n");
            }
        } else {
            fieldName.pseudoClassStateChanged(errorClass, false);
        }

        if (priceMap.containsKey(true)) {
            fieldPrice.pseudoClassStateChanged(errorClass, true);
            List<String> ls = priceMap.get(true);
            for (String error : ls) {
                errors.append(error+"\n");
            }
        } else {
            fieldPrice.pseudoClassStateChanged(errorClass, false);
        }
        labelAlert.setText(String.valueOf(errors));

    }

    void setProductService(ProductService productService) {
        this.productService = productService;
    }

}
