package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {
    private SimpleStringProperty customerName;
    private SimpleStringProperty customerAddress;
    private SimpleStringProperty description;
    private SimpleStringProperty orderType;
    private SimpleIntegerProperty transactionID;
    private BigDecimal totalPrice;
    private LocalDate date;

    public Order() {
        this.customerName = new SimpleStringProperty("");
        this.customerAddress=new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.totalPrice = new BigDecimal(0);
        this.orderType = new SimpleStringProperty("");
        this.transactionID = new SimpleIntegerProperty(0);
        this.date=LocalDate.now();
    }

    public Order(String customerName, String customerAdderss, String description, BigDecimal priceBigDecimal, String orderType, Integer transactionID, LocalDate date) {
        this.customerName = new SimpleStringProperty(customerName);
        this.customerAddress=new SimpleStringProperty(customerAdderss);
        this.description = new SimpleStringProperty(description);
        this.totalPrice = priceBigDecimal;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal priceBigDecimal) {
        this.totalPrice = priceBigDecimal;
    }
}
