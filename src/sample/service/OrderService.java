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

import java.util.function.Predicate;

public class OrderService {
    private OrderDummyRepo orderDummyRepo;

    public OrderService() {
        orderDummyRepo=new OrderDummyRepo();
    }

    public ObservableList getData(){
        return orderDummyRepo.getOrderList();

    }

    public void addData(Order order){
        orderDummyRepo.addOrder(order);
    }

    public void deleteOrderByTransactionId(int transactionId){
       Predicate<Order> orderPredicate=order -> order.getTransactionID()==transactionId;
       orderDummyRepo.deleteOrder(orderPredicate);
    }
}
