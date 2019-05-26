package sample.service;

import javafx.collections.ObservableList;
import sample.model.Order;
import sample.repository.OrderDao;
import sample.repository.OrderDummyRepo;
import sample.repository.impl.OrderProductImpl;

import java.sql.SQLException;

public class OrderService {
    private OrderDao orderDao;

    public OrderService(OrderDao orderDao) {
        this.orderDao =orderDao;
    }

    public ObservableList getOrderList(int pageIndex, int rowsPerPage) {
        return orderDao.getOrderList(pageIndex,rowsPerPage);

    }

    public int addNewOrderToList(Order order) {
        return orderDao.addOrder(order);
    }

    public ObservableList searchOrderById (String id) {
        return orderDao.searchOrderById(id);
    }

    public void deleteOrderByTransactionId(int transactionId) {
        OrderProductService orderProductService = new OrderProductService(new OrderProductImpl());
        //orderProductService.deleteOrderProductByOrderId(transactionId);
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
