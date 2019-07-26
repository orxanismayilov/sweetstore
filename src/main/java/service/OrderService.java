package service;

import javafx.collections.ObservableList;
import model.Order;

import java.util.List;

public interface OrderService {

    ObservableList getOrderList(int pageIndex, int rowsPerPage);

    String addNewOrderToList(Order order);

    List<Order> searchOrderById(String id, boolean searchAll);

    boolean deleteOrderByTransactionId(String transactionId);

    void updateOrderById(Order newOrder, String orderId);

    int getTotalCountOfOrder();
}
