package service;

import javafx.collections.ObservableList;
import sample.model.OrderProduct;
import sample.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderProductService {

    void saveOrderProduct(OrderProduct orderProduct);

    void removeOrderProductById(int id);

    ObservableList getOrderProductByOrderId(int orderId);

    void updateOrderProduct(OrderProduct newOrderProduct, int id);

    Map validateOrderProduct(OrderProduct orderProduct);
}
