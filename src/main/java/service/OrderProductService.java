package service;

import dtos.OrderProductsDTO;
import javafx.collections.ObservableList;
import model.OrderProduct;

import java.util.Map;

public interface OrderProductService {

    void saveOrderProduct(OrderProduct orderProduct);

    void removeOrderProductById(int id,int orderId);

    OrderProductsDTO getOrderProductByOrderId(int orderId);

    void updateOrderProduct(OrderProduct newOrderProduct, int id);

    Map validateOrderProduct(OrderProduct orderProduct);
}
