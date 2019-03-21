package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.model.Product;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    private Product product;
    private static int increment=4;
    @FXML private TextField productName;
    @FXML private TextField productQuantity;
    @FXML private TextField productPrice;
    @FXML private Button buttonSave;



    public void saveProduct(ActionEvent event) throws IOException {

        try {
               product=new Product(increment, productName.getText(), Integer.parseInt(productQuantity.getText()), Double.parseDouble(productPrice.getText()), LocalDate.now());
           } catch (NumberFormatException e) {
               Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter numbers", ButtonType.OK);
               alert.showAndWait();
               return;
           }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "DO YOU WANT TO SAVE?", ButtonType.YES,ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            table.getItems().add(product);
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            stage.close();
            increment++;
        }
    }
    private TableView<Product> table;
    public void setTable(TableView table){
        this.table=table;
    }

    private Stage stage;
    public void setStage(Stage stage){
        this.stage = stage;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
