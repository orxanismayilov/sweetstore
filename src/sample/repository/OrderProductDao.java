package sample.repository;

import javafx.collections.ObservableList;
import sample.model.OrderProduct;

public interface OrderProductDao {

    ObservableList<OrderProduct> getListByOrderId(int orderId);

    void saveOrderProduct(OrderProduct orderProduct);

    void removeOrderProductByOrderId(OrderProduct orderProduct,int orderId);

    void removeOrderProductById(OrderProduct orderProduct,int id);

    OrderProduct doesOrderProductExist(OrderProduct newOrderProduct);

    void updateOrderProduct(OrderProduct newOrderProduct, int id);
}
