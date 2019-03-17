package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import sample.model.Order;

import java.time.LocalDate;

public class OrderDummyRepo {
    private ObservableList<Order> orderList;

    public OrderDummyRepo() {
        populateOrderList();
    }

    public ObservableList getOrderList(){
        return orderList;
    }

    public void addOrder(Order order){
        orderList.add(order);
    }

    public void deleteOrder(Order order){
        orderList.remove(order);
    }

    public void deleteLastOrder(){
        orderList.remove(orderList.size()-1);
    }

    private void populateOrderList(){
        orderList=FXCollections.observableArrayList(
                new Order("Orxan","Mallar",15,"Online",123,LocalDate.of(2015, 02, 20)),
                new Order("Amil","sirniyyat",23,"offline",456, LocalDate.of(1992,12,15)),
                new Order("eltun","guller",456,"dsfjds;",132,LocalDate.of(1627,12,15))
        );
    }
}
