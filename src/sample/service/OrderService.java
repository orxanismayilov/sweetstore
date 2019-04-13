package sample.service;

import javafx.collections.ObservableList;
import sample.model.Order;
import sample.repository.OrderDummyRepo;

public class OrderService {
    private OrderDummyRepo orderDummyRepo;

    public OrderService() {
        orderDummyRepo = new OrderDummyRepo();
    }

    public ObservableList getOrderList() {
        return orderDummyRepo.getOrderList();

    }

    public void addNewOrderToList(Order order) {
        orderDummyRepo.addOrder(order);
    }

    public void deleteOrderByTransactionId(int transactionId) {
        OrderProductService orderProductService = new OrderProductService();
        orderProductService.deleteOrderProductByOrderId(transactionId);
        orderDummyRepo.deleteOrderByTransactionId(transactionId);
    }

    public int getOrderNewId() {
        return orderDummyRepo.getOrderNewId();
    }

}
