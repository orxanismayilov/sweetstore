package service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.OrderProduct;
import model.Product;
import model.ResponseObject;
import service.OrderProductService;
import utils.LoadPropertyUtil;
import utils.RestClientUtil;

import javax.ws.rs.core.Response;
import java.util.*;

public class OrderProductServiceImpl implements OrderProductService {
    private ProductServiceImpl productService;
    private Properties properties;
    private Properties uriProperties;
    private static String ERROR_PROPERTIES="/resources/properties/errors.properties";
    private static String URI_PROPERTIES="/resources/properties/resource-uri.properties";

    public OrderProductServiceImpl(){
        this.productService=new ProductServiceImpl();
        this.properties= LoadPropertyUtil.loadPropertiesFile(ERROR_PROPERTIES);
        this.uriProperties=LoadPropertyUtil.loadPropertiesFile(URI_PROPERTIES);
    }

    public void saveOrderProduct(OrderProduct orderProduct) {
        String uri=uriProperties.getProperty("order-prducturi")+"/list/"+orderProduct.getOrderId();
        RestClientUtil.addNewResource(orderProduct,uri);
    }

    public void removeOrderProductById(int id,int orderId){
        String uri=uriProperties.getProperty("order-prducturi")+"/list/"+orderId;
        RestClientUtil.deleteResource(uri,id);
    }

    public ObservableList getOrderProductByOrderId(int orderId){
        String uri=uriProperties.getProperty("order-prducturi")+"/list/"+orderId;
        List<OrderProduct> list=new ArrayList<>();
        Response response=RestClientUtil.getResourceList(uri);
        if (response.getStatus()==Response.Status.OK.getStatusCode()) {
            ResponseObject responseObject=response.readEntity(ResponseObject.class);
            ObjectMapper mapper=new ObjectMapper();
            list=mapper.convertValue(responseObject.getData(),new TypeReference<List<OrderProduct>>(){});
        }
        return FXCollections.observableArrayList(list);
    }

    public  void updateOrderProduct(OrderProduct newOrderProduct, int id) {
        String uri=uriProperties.getProperty("order-prducturi")+"/list/"+newOrderProduct.getOrderId();
        RestClientUtil.updateResource(uri,id,newOrderProduct);
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
