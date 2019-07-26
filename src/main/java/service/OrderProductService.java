package service;

import javafx.collections.ObservableList;
import model.OrderProduct;

import java.util.Map;

public interface OrderProductService {

    void saveOrderProduct(OrderProduct orderProduct);

    void removeOrderProductById(int id);

    ObservableList getOrderProductByOrderId(int orderId);

    void updateOrderProduct(OrderProduct newOrderProduct, int id);

    Map validateOrderProduct(OrderProduct orderProduct);
}
