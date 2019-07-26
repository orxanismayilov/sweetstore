package repository;

import javafx.collections.ObservableList;
import model.OrderProduct;

public interface OrderProductDao {

    ObservableList<OrderProduct> getListByOrderId(int orderId);

    void saveOrderProduct(OrderProduct orderProduct);

    void removeOrderProductById(int id);

    void updateOrderProduct(OrderProduct newOrderProduct, int id);
}
