package sample.service;

import javafx.collections.ObservableList;
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
       orderDummyRepo.deleteOrderByTransactionId(transactionId);
    }

    public int getOrderNewId() {
        return orderDummyRepo.getOrderNewId();
    }
}
