package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Product;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductInfoController {
    private Product product;
    private Stage stage;
    @FXML private TextField productID;
    @FXML private TextField productName;
    @FXML private TextField quantity;
    @FXML private TextField price;
    @FXML private Button btnCancel;

    public void setProduct(Product product){
        this.product=product;
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }

    public void setFileds() {
        productID.setText(String.valueOf(product.getId()));
        productName.setText(product.getName());
        quantity.setText(String.valueOf(product.getQuantity()));
        price.setText(String.valueOf(product.getPrice()));
    }

    public void cancelAction(ActionEvent event) {
        Stage stage=(Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
