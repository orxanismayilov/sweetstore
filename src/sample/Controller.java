package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML TableView<Order> tableView;
    @FXML TableColumn<Order, String> clmName;
    @FXML TableColumn<Order,Integer> clmTotalprice;
    @FXML TableColumn<Order,String> clmOrdertype;
    @FXML TableColumn<Order,Integer> clmTransactionID;
    @FXML TableColumn<Order,String > clmDescription;
    @FXML Button btnDelete;
    @FXML Button btnNewOrder;


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

    public void addOrder(ActionEvent event){
        Model model=new Model();
        Order order=new Order("das","paxlava",1233,"online",1244845);
        model.addData(order);
        tableView.setItems(model.getData());
    }

    public void deleteData(ActionEvent event){
        Model model=new Model();
        Order order=new Order("Faiq","sfhdsl",15,"fdfh",456);
        model.deleteData(order);
        tableView.setItems(model.getData());
    }

}
