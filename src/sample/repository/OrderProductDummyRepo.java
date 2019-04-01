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

    public void removeOrderProductbyOrderId(int orderId){
        Predicate<OrderProduct> orderProductPredicate=orderProduct -> orderProduct.getOrderId()==orderId;
        removeOrderProduct(orderProductPredicate);
    }

    public void removeOrderProductbyId(int id){
        Predicate<OrderProduct> orderProductPredicate=orderProduct -> orderProduct.getId()==id;
        removeOrderProduct(orderProductPredicate);
    }

    public ObservableList getOrderProductbyOrderId(int orderId){
        ObservableList<OrderProduct> newList=FXCollections.observableArrayList();
        for (OrderProduct orderProduct:list){
            if(orderProduct.getOrderId()==orderId) {
                newList.add(orderProduct);
            }
        }
        return newList;
    }

    public int getOrderProductNewId (){
        int index=list.size()-1;
        if(index<0) return 1;
        return list.get(index).getId()+1;
    }

    private ObservableList copyList(ObservableList<OrderProduct> list){
        ObservableList<OrderProduct> copiedList=FXCollections.observableArrayList();
        for (OrderProduct orderProduct:list){
            copiedList.add(orderProduct);
        }
        return copiedList;
    }
}
