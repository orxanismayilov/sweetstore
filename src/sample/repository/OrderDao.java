package sample.repository;

import javafx.collections.ObservableList;
import sample.model.Order;

import java.sql.SQLException;

public interface OrderDao {

    ObservableList getOrderList(int pageIndex, int rowsPerPage);

    int addOrder(Order order) ;

    void updateOrder(Order newOrder, int oldOrderId);

    void deleteOrderByTransactionId(int transactionId);

    Order getOrderById(int orderId);

    ObservableList searchOrderById(String id);

    int getTotalCountOfOrder() ;
}
