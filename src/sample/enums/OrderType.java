package sample.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum OrderType {
    ONLINE,
    OFFLINE;

    public static ObservableList getOrderTypeList(){
        ObservableList<OrderType> list=FXCollections.observableArrayList(OrderType.values());
        return list;
    }
}
