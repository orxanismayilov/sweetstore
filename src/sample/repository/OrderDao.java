package sample.repository;

import javafx.collections.ObservableList;
import sample.model.Order;

import java.util.List;

public interface OrderDao {

    ObservableList getOrderList(int pageIndex, int rowsPerPage);

    int addOrder(Order order) ;

    void updateOrder(Order newOrder, int oldOrderId);

    void deleteOrderByTransactionId(int transactionId);

    Order getOrderById(int orderId);

    List<Order> searchOrderById(String id,boolean searchAll);

    int getTotalCountOfOrder() ;
}
