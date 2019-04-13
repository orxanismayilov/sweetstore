package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Observable;

public enum OrderType {
    ONLINE,OFFLINE;

    public static ObservableList getOrderTypeList(){
        ObservableList<OrderType> list=FXCollections.observableArrayList();
        list.add(OrderType.ONLINE);
        list.add(OrderType.OFFLINE);
        return list;
    }
}
