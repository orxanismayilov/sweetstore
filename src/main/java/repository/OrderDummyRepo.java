package repository;

import enums.OrderType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Order;
import utils.CopyListUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDummyRepo {
    private static int id = 4;
    private static ObservableList<Order> orderList = FXCollections.observableArrayList(
            new Order("Orxan", "Muxax", "Mallar", BigDecimal.valueOf(15), OrderType.ONLINE, 1, LocalDateTime.now()),
            new Order("Amil", "Baki", "sirniyyat", BigDecimal.valueOf(23), OrderType.OFFLINE, 2, LocalDateTime.now()),
            new Order("Eltun", "Tala", "guller", BigDecimal.valueOf(456), OrderType.OFFLINE, 3, LocalDateTime.now())
    );


    public ObservableList getOrderList() {
        return copyList(order->order.isActive());
    }

    public void addOrder(Order order) {
        order.setDate(LocalDateTime.now());
        orderList.add(0,order);
            }

    public void updateOrder(Order newOrder,int oldOrderId){
        for (Order oldOrder:orderList) {
            if (oldOrder.getTransactionID()==oldOrderId) {
                orderList.remove(oldOrder);
                newOrder.setTransactionID(oldOrderId);
                orderList.add(newOrder);
            }
        }
    }

    public void deleteOrderByTransactionId(int transactionId) {
        for (Order order:orderList){
            if (order.getTransactionID()==transactionId){
                order.setActive(false);
            }
        }
    }

    public int getOrderNewId() {
        return id++;
    }

    public Order getOrderById(int orderId){
        for (Order order:orderList) {
            if (order.getTransactionID()==orderId){
                return order;
            }
        }
        return null;
    }

    public ObservableList searchOrderById(String id) {
        ObservableList list= FXCollections.observableArrayList();
        for (Order order:orderList) {
            if (order.isActive()) {
                if (String.valueOf(order.getTransactionID()).contains(id)) {
                    list.add(order);
                }
            }
        }
        return list;
    }

    private ObservableList copyList(CopyListUtil<Order> rule){
        ObservableList<Order> copiedList=FXCollections.observableArrayList();
        for(Order order:orderList){
           if (rule.check(order)){
               copiedList.add(order);
           }
       }
       return copiedList;
    }

    public int getTotalCountOfOrder() {
        int count=0;
        for (Order o:orderList) {
            count++;
        }
        return count;
    }
}