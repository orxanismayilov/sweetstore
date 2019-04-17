package sample.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum OrderType {
    ONLINE(1,"Onlayn","Online"),
    OFFLINE(2,"Oflayn","Offline");

    private int numOrderType;
    private String azMeaning;
    private String engMeaning;

    OrderType(int i,String azMeaning,String engMeaning) {
        this.numOrderType=i;
        this.azMeaning=azMeaning;
        this.engMeaning=engMeaning;
    }

    public int getNumOrderType() {
        return numOrderType;
    }

    public String getAzMeaning() {
        return azMeaning;
    }

    public String getEngMeaning() {
        return engMeaning;
    }

    public static ObservableList getOrderTypeList(){
        ObservableList<OrderType> list=FXCollections.observableArrayList(OrderType.values());
        return list;
    }
}
