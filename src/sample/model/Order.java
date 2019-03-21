package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {
    private SimpleStringProperty customerName;
    private SimpleStringProperty customerAddress;
    private SimpleStringProperty description;
    private SimpleIntegerProperty totalPrice;
    private SimpleStringProperty orderType;
    private SimpleIntegerProperty transactionID;
    private LocalDate date;

    public Order(String customerName,String customerAdderss, String description, Integer totalPrice, String orderType, Integer transactionID,LocalDate date) {
        this.customerName = new SimpleStringProperty(customerName);
        this.customerAddress=new SimpleStringProperty(customerAdderss);
        this.description = new SimpleStringProperty(description);
        this.totalPrice = new SimpleIntegerProperty(totalPrice);
        this.orderType = new SimpleStringProperty(orderType);
        this.transactionID = new SimpleIntegerProperty(transactionID);
        this.date=date;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getCustomerAddress() {
        return customerAddress.get();
    }

    public SimpleStringProperty customerAddressProperty() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress.set(customerAddress);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public int getTotalPrice() {
        return totalPrice.get();
    }

    public SimpleIntegerProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public String getOrderType() {
        return orderType.get();
    }

    public SimpleStringProperty orderTypeProperty() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType.set(orderType);
    }

    public int getTransactionID() {
        return transactionID.get();
    }

    public SimpleIntegerProperty transactionIDProperty() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID.set(transactionID);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
