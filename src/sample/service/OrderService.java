package sample.service;

import javafx.collections.ObservableList;
import sample.model.Order;
import sample.repository.OrderDao;
import sample.repository.OrderDummyRepo;

import java.sql.SQLException;

public class OrderService {
    private OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao =orderDao;
    }

    public ObservableList getOrderList(int pageIndex, int rowsPerPage) {
        return orderDao.getOrderList(pageIndex,rowsPerPage);

    }

    public void addNewOrderToList(Order order) {
        System.out.println("user"+"added order to db"+order);
        orderDao.addOrder(order);
    }

    public ObservableList searchOrderById (String id) {
        return orderDao.searchOrderById(id);
    }

    public void deleteOrderByTransactionId(int transactionId) throws SQLException {
        OrderProductService orderProductService = new OrderProductService();
        orderProductService.deleteOrderProductByOrderId(transactionId);
        orderDao.deleteOrderByTransactionId(transactionId);
    }

    public void updateOrderById(Order newOrder,int orderId){
        orderDao.updateOrder(newOrder,orderId);
    }

    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    public int getTotalCountOfOrder() {
        return orderDao.getTotalCountOfOrder();
    }
}
