package service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Order;
import model.ResponseObject;
import model.UserSession;
import org.apache.log4j.Logger;
import service.OrderService;
import utils.LoadPropertyUtil;
import utils.RestClientUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

public class OrderServiceImpl implements OrderService {
    private UserSession userSession;
    private Properties uriProperties;
    private static Logger logger=Logger.getLogger(OrderServiceImpl.class);
    private String URI_PROPERTIES="/resources/properties/resource-uri.properties";

    public OrderServiceImpl() {
        this.userSession=UserSession.getInstance();
        this.uriProperties= LoadPropertyUtil.loadPropertiesFile(URI_PROPERTIES);
    }

    public ObservableList getOrderList(int pageIndex, int rowsPerPage) {
        String uri=uriProperties.getProperty("orderuri")+"?pageIndex="+pageIndex+"&maxRows="+rowsPerPage;
        ResponseObject responseObject=RestClientUtil.getResourceList(uri);
        ObjectMapper mapper=new ObjectMapper();
        List<Order> list=mapper.convertValue(responseObject.getData(),new TypeReference<List<Order>>(){});
        return FXCollections.observableArrayList(list);

    }

    public int addNewOrderToList(Order order) {
        String uri=uriProperties.getProperty("orderuri");
        ResponseObject responseObject= RestClientUtil.addNewResource(order,uri);
        Order order1= (Order) responseObject.getData();
        return order1.getTransactionID();
    }

    public ObservableList<Order> searchOrderById (String id,boolean searchAll) {
        String uri=uriProperties.getProperty("orderuri")+"/q?"+"id="+id+"&getAll="+searchAll;
        ResponseObject<List<Order>> responseObject=RestClientUtil.getResourceList(uri);
        ObjectMapper mapper=new ObjectMapper();
        List<Order> list=mapper.convertValue(responseObject.getData(),new TypeReference<List<Order>>(){});
        return FXCollections.observableArrayList(list);
    }

    public boolean deleteOrderByTransactionId(int transactionId) {
        String uri=uriProperties.getProperty("orderuri");
        if (userSession.getUser().getRole()== UserRole.ADMIN){
            RestClientUtil.deleteResource(uri,transactionId);
            logger.info("Order delete started. Order Id:"+transactionId+"user :"+userSession.getUser().toString());
            return true;
        }
        logger.info("Order delete denied user don't have permission for this action. :"+userSession.getUser().toString());
     return false;
    }

    public void updateOrderById(Order newOrder, int orderId){
        String uri=uriProperties.getProperty("orderuri");
        newOrder.setTotalDiscount(new BigDecimal(0));
        RestClientUtil.updateResource(uri,orderId,newOrder);
        logger.info("Order update started. Order :"+orderId+"User :"/*+userSession.getUser().toString()*/);
    }

    public int getTotalCountOfOrder() {
        String uri=uriProperties.getProperty("orderuri");
        ResponseObject<Integer> responseObject=RestClientUtil.getSingleResource(uri,"/count");
        return (int) responseObject.getData();
    }
}
