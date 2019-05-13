package sample.repository;

import javafx.collections.ObservableList;
import sample.model.OrderProduct;

public interface OrderProductDao {

    ObservableList<OrderProduct> getListByOrderId(int orderId);

    void saveOrderProduct(OrderProduct orderProduct);

    void removeOrderProductByOrderId(int orderId);

    void removeOrderProductById(int id);

    OrderProduct doesOrderProductExist(OrderProduct newOrderProduct);

    void updateOrderProduct(OrderProduct newOrderProduct, int id);
}
