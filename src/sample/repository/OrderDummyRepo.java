package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import sample.model.Order;

import java.time.LocalDate;
import java.util.function.Predicate;

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

    public void deleteOrder(Predicate<Order> order){
        orderList.removeIf(order);
    }

    private void populateOrderList(){
        orderList=FXCollections.observableArrayList(
                new Order("Orxan","Muxax","Mallar",15,"Online",123,LocalDate.of(2015, 02, 20)),
                new Order("Amil","Baki","sirniyyat",23,"offline",456, LocalDate.of(1992,12,15)),
                new Order("eltun","Tala","guller",456,"dsfjds;",132,LocalDate.of(1627,12,15))
        );
    }
}
