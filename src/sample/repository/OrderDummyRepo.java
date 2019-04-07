package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import sample.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class OrderDummyRepo {
    private static int id=4;
    private static ObservableList<Order>  orderList=FXCollections.observableArrayList(
            new Order("Orxan","Muxax",new StringBuilder("Mallar"),BigDecimal.valueOf(15),"Online",1,LocalDateTime.now()),
            new Order("Amil","Baki",new StringBuilder("sirniyyat"),BigDecimal.valueOf(23),"offline",2, LocalDateTime.now()),
            new Order("eltun","Tala",new StringBuilder("guller"),BigDecimal.valueOf(456),"dsfjds;",3,LocalDateTime.now())
    );


    public ObservableList getOrderList(){
        return orderList;
    }

    public void addOrder(Order order){
        orderList.add(order);
    }

    public void deleteOrder(Predicate<Order> order){
        orderList.removeIf(order);
    }

    public void deleteOrderByTransactionId(int transactionId){
        Predicate<Order> orderPredicate=order -> order.getTransactionID()==transactionId;
        deleteOrder(orderPredicate);
    }

    public int getOrderNewId(){

        return id++;
    }
}
