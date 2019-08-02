package service;

import dtos.OrdersDTO;
import model.Order;

import java.util.List;

public interface OrderService {

    OrdersDTO getOrderList(int pageIndex, int rowsPerPage);

    int addNewOrderToList(Order order);

    List<Order> searchOrderById(String id, boolean searchAll);

    boolean deleteOrderByTransactionId(int transactionId);

    void updateOrderById(Order newOrder, int orderId);

    int getTotalCountOfOrder();

    Order getOrder(int id);
}
