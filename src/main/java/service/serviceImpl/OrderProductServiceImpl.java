package service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.OrderProductsDTO;
import javafx.collections.FXCollections;
import model.OrderProduct;
import model.Product;
import model.ResponseObject;
import model.UserSession;
import service.OrderProductService;
import utils.LoadPropertyUtil;
import utils.RestClientUtil;

import javax.ws.rs.core.Response;
import java.util.*;

public class OrderProductServiceImpl implements OrderProductService {
    private ProductServiceImpl productService;
    private Properties properties;
    private Properties uriProperties;
    private UserSession session;
    private static String ERROR_PROPERTIES="/resources/properties/errors.properties";
    private static String URI_PROPERTIES="/resources/properties/resource-uri.properties";

    public OrderProductServiceImpl(){
        this.productService=new ProductServiceImpl();
        this.properties= LoadPropertyUtil.loadPropertiesFile(ERROR_PROPERTIES);
        this.uriProperties=LoadPropertyUtil.loadPropertiesFile(URI_PROPERTIES);
        session=UserSession.getInstance();
    }

    public void saveOrderProduct(OrderProduct orderProduct) {
        String uri=uriProperties.getProperty("order-prducturi")+"/list/"+orderProduct.getOrderId();
        RestClientUtil.addNewResource(orderProduct,uri,session.getUser().getName());
    }

    public void removeOrderProductById(int id,int orderId){
        String uri=uriProperties.getProperty("order-prducturi")+"/list/"+orderId;
        RestClientUtil.deleteResource(uri,id,session.getUser().getName());
    }

    public OrderProductsDTO getOrderProductByOrderId(int orderId){
        String uri=uriProperties.getProperty("order-prducturi")+"/list/"+orderId;
        OrderProductsDTO dto=new OrderProductsDTO();
        Response response=RestClientUtil.getResourceList(uri,session.getUser().getName());
        if (response.getStatus()==Response.Status.OK.getStatusCode()) {
            ResponseObject responseObject=response.readEntity(ResponseObject.class);
            ObjectMapper mapper=new ObjectMapper();
            dto=mapper.convertValue(responseObject.getData(),new TypeReference<OrderProductsDTO>(){});
        }
        return dto;
    }

    public  void updateOrderProduct(OrderProduct newOrderProduct, int id) {
        String uri=uriProperties.getProperty("order-prducturi")+"/list/"+newOrderProduct.getOrderId()+"/"+id;
        RestClientUtil.updateResource(uri,newOrderProduct,session.getUser().getName());
    }

    public Map validateOrderProduct(OrderProduct orderProduct) {
        Map<String, Map<Boolean, List<String>>> validation = new HashMap<>();
        Map<Boolean,List<String>> quantityMap=new HashMap<>();
        Map<Boolean,List<String>> discountMap=new HashMap<>();
        Map<Boolean,List<String>> totalPriceMap=new HashMap<>();
        totalPriceMap.put(false,new ArrayList<>());
        quantityMap.put(false,new ArrayList<>());
        discountMap.put(false,new ArrayList<>());
        Product product=productService.getProductById(orderProduct.getProductId());

        if (orderProduct!=null) {
            List quantityList = quantityMap.get(false);
            List discountList = discountMap.get(false);
            List totalPriceList=totalPriceMap.get(false);
            if (product != null) {
                if (orderProduct.getProductQuantity() <= 0) {
                    quantityList.add(properties.getProperty("negativeQuantity"));
                }
                if (orderProduct.getProductQuantity()>product.getQuantity()){
                    quantityList.add(properties.getProperty("possibleQuantity")+"--Current quantity is :"+product.getQuantity());
                }
                if (orderProduct.getDiscount()<0){
                    discountList.add(properties.getProperty("negativeDiscount"));
                }
                if(0 > orderProduct.getTotalPrice().floatValue()){
                    totalPriceList.add(properties.getProperty("negativeTotalPrice"));
                }
            }
            if (!discountList.isEmpty()){
                discountMap.remove(false);
                discountMap.put(true,discountList);
            }

            if (!quantityList.isEmpty()){
                quantityMap.remove(false);
                quantityMap.put(true,quantityList);
            }

            if (!totalPriceList.isEmpty()){
                totalPriceMap.remove(false);
                totalPriceMap.put(true,totalPriceList);
            }
        }
        validation.put("quantityError",quantityMap);
        validation.put("discountError",discountMap);
        validation.put("totalPriceError",totalPriceMap);
        return validation;
    }
}
