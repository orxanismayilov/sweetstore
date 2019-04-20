package sample.enums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public enum OrderStatus {
    DELIVERED(1,"Çatdirildi","Delivered"),
    PENDDING(2,"Gözlemede","Pending"),
    CLOSED(3,"Bağlidir","Closed");

    private int numOrderStatus;
    private String  azMeaning;
    private String engMeaning;

    OrderStatus(int i,String azMeaning,String engMeaning) {
        this.numOrderStatus=i;
        this.azMeaning=azMeaning;
        this.engMeaning=engMeaning;
    }

    public int getNumOrderStatus() {
        return numOrderStatus;
    }

    public String getAzMeaning() {
        return azMeaning;
    }

    public String getEngMeaning() {
        return engMeaning;
    }

    public static ObservableList getOrderStatusList(){
        ObservableList<OrderStatus> list= FXCollections.observableArrayList(OrderStatus.values());
        return list;
    }
}
