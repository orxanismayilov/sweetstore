package model;

import enums.OrderStatus;
import enums.OrderType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private SimpleStringProperty customerName;
    private SimpleStringProperty customerAddress;
    private SimpleStringProperty description;
    private SimpleObjectProperty<OrderType> orderType;
    private SimpleIntegerProperty transactionID;
    private SimpleObjectProperty<BigDecimal> totalPrice;
    private SimpleObjectProperty<BigDecimal> totalDiscount;
    private SimpleObjectProperty<LocalDateTime> date;
    private SimpleObjectProperty<OrderStatus> orderStatus;
    private boolean isActive;

    public Order() {
        this.customerName = new SimpleStringProperty("");
        this.customerAddress=new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.totalPrice = new SimpleObjectProperty<>(new BigDecimal(0));
        this.orderType = new SimpleObjectProperty();
        this.transactionID = new SimpleIntegerProperty(0);
        this.totalDiscount=new SimpleObjectProperty<>(new BigDecimal(0));
        this.date=new SimpleObjectProperty<>(LocalDateTime.now());
        this.orderStatus=new SimpleObjectProperty<>(OrderStatus.PENDDING);
        this.isActive=true;
    }

    public Order(String customerName, String customerAdderss, String description, BigDecimal priceBigDecimal, OrderType orderType, Integer transactionID, LocalDateTime date) {
        this.customerName = new SimpleStringProperty(customerName);
        this.customerAddress=new SimpleStringProperty(customerAdderss);
        this.description = new SimpleStringProperty(description);
        this.totalPrice = new SimpleObjectProperty<>(priceBigDecimal);
        this.orderType = new SimpleObjectProperty<>(orderType);
        this.totalDiscount=new SimpleObjectProperty<>(new BigDecimal(0));
        this.transactionID = new SimpleIntegerProperty(transactionID);
        this.date=new SimpleObjectProperty<>(date);
        this.orderStatus=new SimpleObjectProperty<>(OrderStatus.PENDDING);
        this.isActive=true;
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

    public OrderType getOrderType() {
        return orderType.get();
    }

    public SimpleObjectProperty<OrderType> orderTypeProperty() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
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

    public LocalDateTime getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice.get();
    }

    public SimpleObjectProperty<BigDecimal> totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount.get();
    }

    public SimpleObjectProperty<BigDecimal> totalDiscountProperty() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount.set(totalDiscount);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus.get();
    }

    public SimpleObjectProperty<OrderStatus> orderStatusProperty() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus.set(orderStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
                "customerName=" + customerName +
                ", description='" + description +
                ", orderType=" + orderType +
                ", transactionID=" + transactionID +
                ", totalPrice=" + totalPrice +
                ", totalDiscount=" + totalDiscount +
                ", date=" + date +
                ", isActive=" + isActive +
                '}';
    }
}
