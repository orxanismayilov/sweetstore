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
import sample.utils.NumberUtils;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    private Product product;
    private ProductService productService;
    private Map<String, Map<Boolean, List<String>>> validation;
    private final static String QUANTITY_ERROR = "Quantity must be number";
    private final static String PRICE_ERROR = "Price must be number";
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");
    @FXML
    private TextField productName;
    @FXML
    private TextField productQuantity;
    @FXML
    private TextField productPrice;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private Label lblAlert;


    public void saveProduct() {
        product = new Product();
        product.setName(productName.getText());
        try {
            product.setQuantity(Integer.parseInt(productQuantity.getText()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            lblAlert.setText(QUANTITY_ERROR);
            productQuantity.pseudoClassStateChanged(errorClass, true);
            return;
        }

        try {
            product.setPrice(Float.parseFloat(productPrice.getText()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            lblAlert.setText(PRICE_ERROR);
            productPrice.pseudoClassStateChanged(errorClass, true);
            return;
        }

        validation = productService.addProduct(product);

        if (!validation.get("nameError").containsKey(true) && !validation.get("quantityError").containsKey(true) && !validation.get("priceError").containsKey(true)) {
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            stage.close();
        } else {
            handleErrors(validation);
        }
    }

    public void cancelAction() {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validatePrice();
        validateQuantity();
    }


    private void validatePrice() {
        productPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (NumberUtils.isNumberFloat(newValue)) {
                productPrice.pseudoClassStateChanged(errorClass, false);
            } else {
                productPrice.pseudoClassStateChanged(errorClass, true);
            }
        });
    }

    private void validateQuantity() {
        productQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (NumberUtils.isNumberInteger(newValue)) {
                productQuantity.pseudoClassStateChanged(errorClass, false);
            } else {
                productQuantity.pseudoClassStateChanged(errorClass, true);
            }
        });
    }

    private void handleErrors(Map<String, Map<Boolean, List<String>>> validation) {
        Map<Boolean, List<String>> nameMap = validation.get("nameError");
        Map<Boolean, List<String>> quantityMap = validation.get("quantityError");
        Map<Boolean, List<String>> priceMap = validation.get("priceError");
        StringBuilder errors = new StringBuilder();
        if (nameMap.containsKey(true)) {
            productName.pseudoClassStateChanged(errorClass, true);
            List<String> ls = nameMap.get(true);
            for (String error : ls) {
                errors.append(error);
            }

        } else {
            productName.pseudoClassStateChanged(errorClass, false);
        }
        if (quantityMap.containsKey(true)) {
            productQuantity.pseudoClassStateChanged(errorClass, true);
            List<String> ls = quantityMap.get(true);
            for (String error : ls) {
                errors.append(error);
            }
        } else {
            productQuantity.pseudoClassStateChanged(errorClass, false);
        }
        if (priceMap.containsKey(true)) {
            productPrice.pseudoClassStateChanged(errorClass, true);
            List<String> ls = priceMap.get(true);
            for (String error : ls) {
                errors.append(error);
            }
        } else {
            productPrice.pseudoClassStateChanged(errorClass, false);
        }
        lblAlert.setText(String.valueOf(errors));

    }
}

