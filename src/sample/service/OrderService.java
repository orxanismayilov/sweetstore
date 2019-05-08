package sample.service;

import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Order;
import sample.repository.OrderDummyRepo;

import java.sql.SQLException;

public class OrderService {
    private OrderDummyRepo orderDummyRepo;

    public OrderService() {
        orderDummyRepo = new OrderDummyRepo();
    }

    public ObservableList getOrderList() {
        return orderDummyRepo.getOrderList();

    }

    public void addNewOrderToList(Order order) {
        System.out.println("user"+"added order to db"+order);
        orderDummyRepo.addOrder(order);
    }

    public ObservableList searchOrderById (String id) {
        return orderDummyRepo.searchOrderById(id);
    }

    public void deleteOrderByTransactionId(int transactionId) throws SQLException {
        OrderProductService orderProductService = new OrderProductService();
        orderProductService.deleteOrderProductByOrderId(transactionId);
        orderDummyRepo.deleteOrderByTransactionId(transactionId);
    }

    public void updateOrderById(Order newOrder,int orderId){
        orderDummyRepo.updateOrder(newOrder,orderId);
    }

    public Order getOrderById(int orderId) {
        return orderDummyRepo.getOrderById(orderId);
    }

    public int getOrderNewId() {
        return orderDummyRepo.getOrderNewId();
    }

    public int getTotalCountOfOrder() {
        return orderDummyRepo.getTotalCountOfOrder();
    }
}
