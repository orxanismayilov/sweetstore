package service.serviceImpl;

import javafx.collections.ObservableList;
import model.OrderProduct;
import model.Product;
import repository.OrderProductDao;
import repository.impl.ProductDaoImpl;
import service.OrderProductService;
import utils.LoadPropertyUtil;

import java.util.*;

public class OrderProductServiceImpl implements OrderProductService {
    private OrderProductDao orderProductDao;
    private ProductServiceImpl productService;
    private Properties properties;
    Map<String,Map<Boolean,List<String>>> validation;
    private static String ERROR_PROPERTIES="/resources/properties/errors.properties";

    public OrderProductServiceImpl(OrderProductDao orderProductDao){
        this.orderProductDao = orderProductDao;
        this.productService=new ProductServiceImpl(new ProductDaoImpl());
        this.properties= LoadPropertyUtil.loadPropertiesFile(ERROR_PROPERTIES);
    }

    public void saveOrderProduct(OrderProduct orderProduct) {
        orderProductDao.saveOrderProduct(orderProduct);
    }

    public void removeOrderProductById(int id){
        orderProductDao.removeOrderProductById(id);
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
