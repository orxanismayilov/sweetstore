package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.model.Product;
import sample.service.ProductService;
import sample.utils.ScreenUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static sample.utils.AddButtonTableUtils.addButtonToTable;

public class StockController implements Initializable {
    private ProductService productService;
    private Stage fxmlControllerStage;
    private final static String FXML_URL_HOMEPAGE="../resource/screens/homepage.fxml";
    private final static String FXML_URL_NEWPRODUCT="../resource/screens/addproduct.fxml";
    @FXML private TableView <Product> tableProduct;
    @FXML private TableColumn<Product,Integer> clmID;
    @FXML private TableColumn<Product,String> clmName;
    @FXML private TableColumn<Product,Double> clmPrice;
    @FXML private TableColumn<Product,LocalDate> clmLastUpdate;
    @FXML private TableColumn<Product,Integer> clmQuantity;
    @FXML private TableColumn<Object,Void> clmAction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService=new ProductService();
        populateTable();
        tableProduct.refresh();
    }

    private void populateTable() {
        clmID.setCellValueFactory(new PropertyValueFactory<Product,Integer>("id"));
        clmName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        clmPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        clmLastUpdate.setCellValueFactory(new PropertyValueFactory<Product, LocalDate>("updateDate"));
        clmQuantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        clmAction.setCellFactory(addButtonToTable(clmAction, clmID, productService));
        clmID.setMaxWidth(100);
        clmID.setMinWidth(60);
        clmAction.setResizable(false);
        clmAction.setMinWidth(120);
        clmAction.setMaxWidth(120);
        tableProduct.setItems(productService.getData());
    }

    public void btnNewProductAction(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_NEWPRODUCT));
        try{
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root,500,300));
            if(loader.getController() instanceof AddProductController){
                AddProductController addProductController = loader.getController();
                addProductController.setStage(fxmlControllerStage);
                addProductController.setTable(tableProduct);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        fxmlControllerStage.setTitle("Update");
        fxmlControllerStage.setResizable(false);
        fxmlControllerStage.show();
    }

    public void btnBackAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event, FXML_URL_HOMEPAGE);
    }
}
