package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.model.Product;
import sample.service.ProductService;

import java.net.URL;
import java.util.ResourceBundle;

public class NewOrderController implements Initializable {
    private ProductService productService;
    private ObservableList<Product> list;
    @FXML private ComboBox comboBoxProducts;
    @FXML private TableView tableView;
    @FXML private TableColumn<Product,Integer> columnId;
    @FXML private TableColumn<Product,String> columnName;
    @FXML private TableColumn<Product,Integer> columnQuantity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService=new ProductService();
        tableView.setItems(list);
    }

    public void fillComboBox(){
        ObservableList<String> products = FXCollections.observableArrayList();
        ObservableList<Product> list=productService.getData();
        for(Product product:list){
            products.add(product.getName());
        }
        comboBoxProducts.setItems(products);
    }
}
