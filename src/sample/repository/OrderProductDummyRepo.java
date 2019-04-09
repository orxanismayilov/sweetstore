package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.OrderProduct;
import sample.utils.CopyListUtil;

import java.util.function.Predicate;

public class OrderProductDummyRepo {
    private static int newId = 1;
    private static ObservableList<OrderProduct> list = FXCollections.observableArrayList();

    public ObservableList<OrderProduct> getList() {
        return copyList(orderProduct -> orderProduct.isActive());
    }

    public void addOrderProductToList(OrderProduct orderProduct) {
        list.add(orderProduct);
    }

    private void removeOrderProduct(Predicate<OrderProduct> predicate) {
        list.removeIf(predicate);
    }

    public void removeOrderProductByOrderId(int orderId) {
        for (OrderProduct orderProduct : list) {
            if (orderProduct.getOrderId() == orderId) {
                orderProduct.setActive(false);
            }
        }
    }

    public void removeOrderProductById(int id) {
        Predicate<OrderProduct> orderProductPredicate = orderProduct -> orderProduct.getId() == id;
        removeOrderProduct(orderProductPredicate);
    }

    public ObservableList getOrderProductByOrderId(int orderId) {
        ObservableList<OrderProduct> newList = FXCollections.observableArrayList();
        for (OrderProduct orderProduct : list) {
            if (orderProduct.getOrderId() == orderId) {
                newList.add(orderProduct);
            }
        }
        return newList;
    }

    public int getOrderProductNewId() {
        return newId++;
    }

    private ObservableList copyList(CopyListUtil<OrderProduct> rule) {
        ObservableList<OrderProduct> copiedList = FXCollections.observableArrayList();
        for (OrderProduct orderProduct : list) {
            if (rule.check(orderProduct)) {
                copiedList.add(orderProduct);
            }
        }
        return copiedList;
    }
}
