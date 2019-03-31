package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import sample.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Predicate;

public class OrderDummyRepo {
    private static ObservableList<Order>  orderList=FXCollections.observableArrayList(
            new Order("Orxan","Muxax",new StringBuilder("Mallar"),BigDecimal.valueOf(15),"Online",123,LocalDate.of(2015, 02, 20)),
            new Order("Amil","Baki",new StringBuilder("sirniyyat"),BigDecimal.valueOf(23),"offline",456, LocalDate.of(1992,12,15)),
            new Order("eltun","Tala",new StringBuilder("guller"),BigDecimal.valueOf(456),"dsfjds;",132,LocalDate.of(1627,12,15))
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
        int index=orderList.size()-1;
        return orderList.get(index).getTransactionID()+1;
    }
}
