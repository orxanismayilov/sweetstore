package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.model.Product;


public class ProductInfoController {

    private String manatSymbol = "\u20BC";
    @FXML
    private Label labelId;
    @FXML
    private Label labelName;
    @FXML
    private Label labelQuantity;
    @FXML
    private Label labelPrice;
    @FXML
    private Button btnCancel;

    public void cancelAction() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();

    }

    void setFields(Product product) {
        labelId.setText(String.valueOf(product.getId()));
        labelName.setText(product.getName());
        labelQuantity.setText(String.valueOf(product.getQuantity()));
        labelPrice.setText(String.valueOf(product.getPrice() + manatSymbol));
    }
}
