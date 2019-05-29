package sample.service;

import javafx.collections.ObservableList;
import sample.enums.UserRole;
import sample.model.Order;
import sample.model.UserSession;
import sample.repository.OrderDao;

import java.util.List;

public class OrderService {
    private OrderDao orderDao;
    private UserSession userSession;
    public OrderService(OrderDao orderDao) {
        this.orderDao =orderDao;
        this.userSession=UserSession.getInstance();
    }

    public ObservableList getOrderList(int pageIndex, int rowsPerPage) {
        return orderDao.getOrderList(pageIndex,rowsPerPage);

    }

    public int addNewOrderToList(Order order) {
        return orderDao.addOrder(order);
    }

    public List<Order> searchOrderById (String id,boolean searchAll) {
        searchAll=userSession.getUser().getRole()== UserRole.ADMIN ? true:false;
        return orderDao.searchOrderById(id,searchAll);
    }

    public void deleteOrderByTransactionId(int transactionId) {
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
