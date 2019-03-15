package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML TableView<Order> tableView;
    @FXML TableColumn<Order, String> clmName;
    @FXML TableColumn<Order,Integer> clmTotalprice;
    @FXML TableColumn<Order,String> clmOrdertype;
    @FXML TableColumn<Order,Integer> clmTransactionID;
    @FXML TableColumn<Order,String > clmDescription;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Model model=new Model();
        clmName.setCellValueFactory(new PropertyValueFactory<Order,String>("name"));
        clmDescription.setCellValueFactory(new PropertyValueFactory<Order,String >("description"));
        clmTotalprice.setCellValueFactory(new PropertyValueFactory<Order,Integer>("price"));
        clmOrdertype.setCellValueFactory(new PropertyValueFactory<Order, String>("type"));
        clmTransactionID.setCellValueFactory(new PropertyValueFactory<Order,Integer>("transactionid"));
        tableView.setItems(model.getData());
    }

}
