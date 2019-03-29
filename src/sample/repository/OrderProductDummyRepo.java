package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.OrderProduct;

import java.util.function.Predicate;

public class OrderProductDummyRepo {
    private static ObservableList<OrderProduct> list=FXCollections.observableArrayList();

    public  ObservableList<OrderProduct> getList() {
        return copyList(list);
    }

    public void addOrderProducttoList(OrderProduct orderProduct){
        list.add(orderProduct);
    }

    public void removeOrderProduct(Predicate<OrderProduct> predicate){
        list.removeIf(predicate);
    }

    public void removeOrderPrductbyOrderId(int orderId){
        Predicate<OrderProduct> orderProductPredicate=orderProduct -> orderProduct.getOrderId()==orderId;
        removeOrderProduct(orderProductPredicate);

    }

    private ObservableList copyList(ObservableList<OrderProduct> list){
        ObservableList<OrderProduct> copiedList=FXCollections.observableArrayList();
        for (OrderProduct orderProduct:list){
            copiedList.add(orderProduct);
        }
        return copiedList;
    }
}
