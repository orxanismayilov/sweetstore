package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import sample.model.Product;
import sample.service.ProductService;

import java.net.URL;
import java.util.ResourceBundle;

public class NewOrderController implements Initializable {
    private ProductService productService;
    @FXML private ComboBox comboBoxProducts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService=new ProductService();
    }

    public void fillProductList(){
        ObservableList<String> products = FXCollections.observableArrayList();
        ObservableList<Product> list=productService.getData();
        for(Product product:list){
            products.add(product.getName());
        }
        comboBoxProducts.setItems(products);
    }
}
