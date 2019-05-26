package sample.service;

import javafx.collections.ObservableList;
import sample.model.OrderProduct;
import sample.model.Product;
import sample.repository.OrderProductDao;
import sample.repository.impl.ProductDaoImpl;
import sample.utils.LoadPropertyUtil;

import java.util.*;

public class OrderProductService {
    private OrderProductDao orderProductDao;
    private ProductService productService;
    private Properties properties;
    Map<String,Map<Boolean,List<String>>> validation;
    private static String ERROR_PROPERTIES="C:\\Users\\Orxan\\Desktop\\Home Project\\Home Project\\src\\sample\\resource\\properties\\errors.properties";

    public OrderProductService(OrderProductDao orderProductDao){
        this.orderProductDao = orderProductDao;
        this.productService=new ProductService(new ProductDaoImpl());
        this.properties= LoadPropertyUtil.loadPropertiesFile(ERROR_PROPERTIES);
    }

    public void saveOrderProduct(OrderProduct orderProduct) {
        orderProductDao.saveOrderProduct(orderProduct);
    }

    void deleteOrderProductByOrderId(OrderProduct orderProduct,int orderId){
        orderProductDao.removeOrderProductByOrderId(orderProduct,orderId);
    }

    public void removeOrderProductById(OrderProduct orderProduct,int id){
        orderProductDao.removeOrderProductById(orderProduct,id);
    }

    public ObservableList getOrderProductByOrderId(int orderId){
        return orderProductDao.getListByOrderId(orderId);
    }

    public  void updateOrderProduct(OrderProduct newOrderProduct, int id) {
        orderProductDao.updateOrderProduct(newOrderProduct,id);
    }

    public Map validateOrderProduct(OrderProduct orderProduct) {
        validation=new HashMap<>();
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
