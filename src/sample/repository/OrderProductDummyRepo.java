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
        orderProduct.setId(getOrderProductNewId());
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

    public OrderProduct doesOrderProductExist(OrderProduct newOrderProduct){
        for (OrderProduct orderProduct:list){
            if (orderProduct.getId()==newOrderProduct.getId()){
                if (orderProduct.isActive()){
                    return orderProduct;
                }
            }
        }
        return null;
    }

    public void updateOrderProduct(OrderProduct newOrderProduct, int id){
        for (OrderProduct oldOrderProduct:list){
            if(oldOrderProduct.getId()==id){
                newOrderProduct.setId(id);
                //oldOrderProduct.set
                list.remove(oldOrderProduct);
                list.add(newOrderProduct);
            }
        }
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
