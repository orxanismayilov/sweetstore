package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML TableView<Order> tableView;
    @FXML TableColumn<Order, String> clmName;
    @FXML TableColumn<Order,Integer> clmTotalprice;
    @FXML TableColumn<Order,String> clmOrdertype;
    @FXML TableColumn<Order,Integer> clmTransactionID;
    @FXML TableColumn<Order,String > clmDescription;

    ObservableList<Order> data=FXCollections.observableArrayList(
            new Order("Orxan","Mallar",15,"Online",123),
            new Order("Amil","sirniyyat",23,"offline",456),
            new Order("eltun","guller",456,"dsfjds;",132)
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clmName.setCellValueFactory(new PropertyValueFactory<Order,String>("Name"));
        clmDescription.setCellValueFactory(new PropertyValueFactory<Order,String >("Description"));
        clmTotalprice.setCellValueFactory(new PropertyValueFactory<Order,Integer>("Price"));
        clmOrdertype.setCellValueFactory(new PropertyValueFactory<Order, String>("Type"));
        clmTransactionID.setCellValueFactory(new PropertyValueFactory<Order,Integer>("TransactionID"));
        tableView.setItems(data);
    }
}
