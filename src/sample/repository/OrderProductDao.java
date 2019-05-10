package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.OrderProduct;
import sample.utils.CopyListUtil;

import java.util.function.Predicate;

public interface OrderProductDao {
    ObservableList<OrderProduct> getList();

    void addOrderProductToList(OrderProduct orderProduct);

    void removeOrderProductByOrderId(int orderId);

    void removeOrderProductById(int id);

    OrderProduct doesOrderProductExist(OrderProduct newOrderProduct);

    void updateOrderProduct(OrderProduct newOrderProduct, int id);

    ObservableList getOrderProductByOrderId(int orderId);
}
