package sample.service;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.model.Order;
import sample.repository.OrderDummyRepo;

public class OrderService {
    private OrderDummyRepo orderDummyRepo;
    private int constatIncreament;

    public OrderService() {
        orderDummyRepo=new OrderDummyRepo();
        constatIncreament=30;
    }

    public ObservableList getData(){
        return orderDummyRepo.getOrderList();

    }
    public void deleteOrderByTransactionId(int transactionId){
    // TODO: FILL INSIDE

    }
    public void deleteDataLast(){
        orderDummyRepo.deleteLastOrder();
    }

    public void addData(Order order){
        order.setTotalprice(order.getTotalPrice()+ ++constatIncreament);
        orderDummyRepo.addOrder(order);
    }

}
