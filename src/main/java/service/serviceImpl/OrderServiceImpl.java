package service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.OrdersDTO;
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

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OrderServiceImpl implements OrderService {
    private UserSession session;
    private Properties uriProperties;
    private static Logger logger=Logger.getLogger(OrderServiceImpl.class);
    private String URI_PROPERTIES="/resources/properties/resource-uri.properties";

    public OrderServiceImpl() {
        this.session =UserSession.getInstance();
        this.uriProperties= LoadPropertyUtil.loadPropertiesFile(URI_PROPERTIES);
    }

    public OrdersDTO getOrderList(int pageIndex, int rowsPerPage) {
        String uri=uriProperties.getProperty("orderuri")+"?pageIndex="+pageIndex+"&maxRows="+rowsPerPage;
        OrdersDTO ordersDTO=new OrdersDTO();
        Response response=RestClientUtil.getResourceList(uri,session.getUser().getName());
        if (response.getStatus()==Response.Status.OK.getStatusCode()) {
            ResponseObject responseObject=response.readEntity(ResponseObject.class);
            ObjectMapper mapper = new ObjectMapper();
            ordersDTO = mapper.convertValue(responseObject.getData(), new TypeReference<OrdersDTO>() {});
        }
        return ordersDTO;
    }

    public int addNewOrderToList(Order order) {
        String uri=uriProperties.getProperty("orderuri");
        Response response= RestClientUtil.addNewResource(order,uri,session.getUser().getName());
        if (response.getStatus()==Response.Status.CREATED.getStatusCode()) {
            ResponseObject responseObject=response.readEntity(ResponseObject.class);
            ObjectMapper mapper=new ObjectMapper();
            Order order1 = mapper.convertValue(responseObject.getData(),new TypeReference<Order>(){});
            return order1.getId();
        }
        return 0;
    }

    public ObservableList<Order> searchOrderById (String id,boolean searchAll) {
        String uri=uriProperties.getProperty("orderuri")+"/q?"+"id="+id+"&getAll="+searchAll;
        Response response=RestClientUtil.getResourceList(uri,session.getUser().getName());
        List<Order> list=new ArrayList<>();
        if (response.getStatus()==Response.Status.OK.getStatusCode()) {
            ResponseObject responseObject = response.readEntity(ResponseObject.class);
            ObjectMapper mapper = new ObjectMapper();
            list = mapper.convertValue(responseObject.getData(), new TypeReference<List<Order>>() {
            });
        }
        return FXCollections.observableArrayList(list);
    }

    public boolean deleteOrderByTransactionId(int transactionId) {
        String uri=uriProperties.getProperty("orderuri");
        if (session.getUser().getRole()== UserRole.ADMIN){
            RestClientUtil.deleteResource(uri,transactionId,session.getUser().getName());
            logger.info("Order delete started. Order Id:"+transactionId+"user :"+ session.getUser().toString());
            return true;
        }
        logger.info("Order delete denied user don't have permission for this action. :"+ session.getUser().toString());
     return false;
    }

    public void updateOrderById(Order newOrder, int orderId){
        String uri=uriProperties.getProperty("orderuri");
        newOrder.setTotalDiscount(new BigDecimal(0));
        RestClientUtil.updateResource(uri,orderId,newOrder,session.getUser().getName());
        logger.info("Order update started. Order :"+orderId+"User :"/*+session.getUser().toString()*/);
    }

    public int getTotalCountOfOrder() {
        String uri=uriProperties.getProperty("orderuri");
       Response response=RestClientUtil.getSingleResource(uri,"/count",session.getUser().getName());
       if (response.getStatus()==Response.Status.OK.getStatusCode()) {
           ResponseObject responseObject =response.readEntity(ResponseObject.class);
           return (int) responseObject.getData();
       }
       return 0;
    }

    @Override
    public Order getOrder(int id) {
        String uri=uriProperties.getProperty("orderuri");
        Response response=RestClientUtil.getSingleResource(uri, String.valueOf(id),session.getUser().getName());
        if (response.getStatus()==Response.Status.OK.getStatusCode()) {
            ResponseObject responseObject=response.readEntity(ResponseObject.class);
            ObjectMapper mapper=new ObjectMapper();
            Order order=mapper.convertValue(responseObject.getData(),new TypeReference<Order>(){});
            return order;
        }
        return null;
    }
}
