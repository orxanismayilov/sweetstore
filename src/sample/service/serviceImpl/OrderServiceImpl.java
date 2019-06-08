package sample.service.serviceImpl;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.enums.UserRole;
import sample.model.Order;
import sample.model.UserSession;
import sample.repository.OrderDao;
import sample.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao;
    private UserSession userSession;
    private static Logger logger=Logger.getLogger(OrderServiceImpl.class);
    public OrderServiceImpl(OrderDao orderDao) {
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
        return orderDao.searchOrderById(id,searchAll);
    }

    public boolean deleteOrderByTransactionId(int transactionId) {
        if (userSession.getUser().getRole()==UserRole.ADMIN){
            orderDao.deleteOrderByTransactionId(transactionId);
            logger.info("Order delete started. Order Id:"+transactionId+"user :"+userSession.getUser().toString());
            return true;
        }
        logger.info("Order delete denied user don't have permission for this action. :"+userSession.getUser().toString());
     return false;
    }

    public void updateOrderById(Order newOrder,int orderId){
        orderDao.updateOrder(newOrder,orderId);
        logger.info("Order update started. Order :"+orderId+"User :"+userSession.getUser().toString());
    }

    public int getTotalCountOfOrder() {
        return orderDao.getTotalCountOfOrder();
    }
}
