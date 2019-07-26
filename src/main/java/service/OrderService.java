package service;

import javafx.collections.ObservableList;
import model.Order;

import java.util.List;

public interface OrderService {

    ObservableList getOrderList(int pageIndex, int rowsPerPage);

    int addNewOrderToList(Order order);

    List<Order> searchOrderById(String id, boolean searchAll);

    boolean deleteOrderByTransactionId(int transactionId);

    void updateOrderById(Order newOrder, int orderId);

    int getTotalCountOfOrder();
}
