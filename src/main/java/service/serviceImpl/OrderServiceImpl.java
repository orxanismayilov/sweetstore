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
        Response response=RestClientUtil.getResourceList(uri,session.getClientConfig());
        if (response.getStatus()==Response.Status.OK.getStatusCode()) {
            ResponseObject responseObject=response.readEntity(ResponseObject.class);
            ObjectMapper mapper = new ObjectMapper();
            ordersDTO = mapper.convertValue(responseObject.getData(), new TypeReference<OrdersDTO>() {});
        }
        return ordersDTO;
    }

    public int addNewOrderToList(Order order) {
        String uri=uriProperties.getProperty("orderuri");
        order.setDate("");
        Response response= RestClientUtil.addNewResource(order,uri,session.getClientConfig());
        if (response.getStatus()==Response.Status.CREATED.getStatusCode()) {
            ResponseObject responseObject=response.readEntity(ResponseObject.class);
            ObjectMapper mapper=new ObjectMapper();
            Order order1 = mapper.convertValue(responseObject.getData(),new TypeReference<Order>(){});
            return order1.getId();
        }
        return 0;
    }

    public ObservableList<Order> searchOrderById (String id,boolean searchAll) {
        String uri=uriProperties.getProperty("orderuri")+"/search?"+"id="+id+"&getAll="+searchAll;
        Response response=RestClientUtil.getResourceList(uri,session.getClientConfig());
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
            Response response=RestClientUtil.deleteResource(uri,transactionId,session.getClientConfig());
            if (response.getStatus()==Response.Status.NO_CONTENT.getStatusCode()) {
                logger.info("Order delete started. Order Id:" + transactionId + "user :" + session.getClientConfig().toString());
                return true;
            }
            logger.warn("Permission denied.");
            return false;
    }

    public void updateOrderById(Order newOrder, int orderId){
        String uri=uriProperties.getProperty("orderuri")+"/"+orderId;
        RestClientUtil.updateResource(uri,newOrder,session.getClientConfig());
        logger.info("Order update started. Order :"+orderId+"User :");
    }

    public int getTotalCountOfOrder() {
        String uri=uriProperties.getProperty("orderuri");
       Response response=RestClientUtil.getSingleResource(uri,"/count",session.getClientConfig());
       if (response.getStatus()==Response.Status.OK.getStatusCode()) {
           ResponseObject responseObject =response.readEntity(ResponseObject.class);
           return (int) responseObject.getData();
       }
       return 0;
    }

    @Override
    public Order getOrder(int id) {
        String uri=uriProperties.getProperty("orderuri");
        Response response=RestClientUtil.getSingleResource(uri, String.valueOf(id),session.getClientConfig());
        if (response.getStatus()==Response.Status.OK.getStatusCode()) {
            ResponseObject responseObject=response.readEntity(ResponseObject.class);
            ObjectMapper mapper=new ObjectMapper();
            return mapper.convertValue(responseObject.getData(),new TypeReference<Order>(){});
        }
        return null;
    }
}
