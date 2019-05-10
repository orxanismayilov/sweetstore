package sample.service;

import javafx.collections.ObservableList;
import sample.model.OrderProduct;
import sample.model.Product;
import sample.repository.OrderProductDummyRepo;
import sample.repository.impl.ProductDaoImpl;
import sample.utils.LoadPropertyUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public class OrderProductService {
    private OrderProductDummyRepo orderProductDummyRepo;
    private Map<String,Map<Boolean,List<String>>> validation;
    private ProductService productService;
    private Properties properties;
    private static String ERROR_PROPERTIES="C:\\Users\\Orxan\\Desktop\\Home Project\\Home Project\\src\\sample\\resource\\properties\\errors.properties";

    public OrderProductService(){
        this.orderProductDummyRepo = new OrderProductDummyRepo();
        this.productService=new ProductService(new ProductDaoImpl());
        this.properties= LoadPropertyUtil.loadPropertiesFile(ERROR_PROPERTIES);
    }

    public ObservableList getOrderProductList(){
        return orderProductDummyRepo.getList();
    }

    public Map addOrderProductToList(OrderProduct orderProduct) throws Exception {
        validation=validateOrderProduct(orderProduct);
        if (!validation.get("quantityError").containsKey(true) &&!validation.get("discountError").containsKey(true) && !validation.get("totalPriceError").containsKey(true)){
            OrderProduct op=orderProductDummyRepo.doesOrderProductExist(orderProduct);
            Product product = productService.getProductById(orderProduct.getProductId());
            if (op==null) {
                product.setQuantity(product.getQuantity() - orderProduct.getProductQuantity());
                productService.updateProduct(product, product.getId());
                orderProductDummyRepo.addOrderProductToList(orderProduct);
            } else {
                product.setQuantity(product.getQuantity()-orderProduct.getProductQuantity());
                productService.updateProduct(product,product.getId());
                orderProductDummyRepo.updateOrderProduct(orderProduct,op.getId());
            }
        }
        return validation;
    }

    public void deleteOrderProductByOrderId(int orderId){
        orderProductDummyRepo.removeOrderProductByOrderId(orderId);
    }

    public void removeOrderProductByProductId(int productId){
        orderProductDummyRepo.removeOrderProductById(productId);
    }

    public ObservableList getOrderProductByOrderId(int orderId){
        return orderProductDummyRepo.getOrderProductByOrderId(orderId);
    }

    private Map validateOrderProduct(OrderProduct orderProduct) throws Exception {
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
                if (orderProduct.getProductQuantity() < 0) {
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
