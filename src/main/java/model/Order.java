package model;

import enums.OrderStatus;
import enums.OrderType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
public class Order {
    private String customerName;
    private String customerAddress;
    private String description;
    private OrderType orderType;
    private int transactionID;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;
    private String date;
    private OrderStatus orderStatus;
    private boolean isActive;

    public Order() {
    }

    public Order(String customerName, String customerAdderss, String description, BigDecimal priceBigDecimal, BigDecimal totalDiscount, OrderStatus orderStatus, OrderType orderType, int transactionID, String date) {
        this.customerName = customerName;
        this.customerAddress=customerAdderss;
        this.description =description;
        this.totalPrice = priceBigDecimal;
        this.orderType = orderType;
        this.totalDiscount=totalDiscount;
        this.transactionID = transactionID;
        this.date=date;
        this.orderStatus=orderStatus;
        this.isActive=true;
    }

    public String getCustomerName() {
        return customerName;
    }

    public SimpleStringProperty customerNameProperty() {
        return new SimpleStringProperty(customerName);
    }

    public void setCustomerName(String customerName) {
        this.customerName=customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public SimpleStringProperty customerAddressProperty() {
        return  new SimpleStringProperty(customerAddress);
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress=customerAddress;
    }

    public String getDescription() {
        return description;
    }

    public SimpleStringProperty descriptionProperty() {
        return new SimpleStringProperty(description);
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public SimpleObjectProperty<OrderType> orderTypeProperty() {
        return new SimpleObjectProperty<>(orderType);
    }

    public void setOrderType(OrderType orderType) {
        this.orderType=orderType;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public SimpleIntegerProperty transactionIDProperty() {
        return new SimpleIntegerProperty(transactionID);
    }

    public void setTransactionID(int transactionID) {
        this.transactionID=transactionID;
    }

    public String getDate() {
        return date;
    }

    public SimpleStringProperty dateProperty() {
        return new SimpleStringProperty(date);
    }

    public void setDate(String date) {
        this.date=date;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public SimpleObjectProperty<BigDecimal> totalPriceProperty() {
        return new SimpleObjectProperty<>(totalPrice);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice=totalPrice;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public SimpleObjectProperty<BigDecimal> totalDiscountProperty() {
        return new SimpleObjectProperty<>(totalDiscount);
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount=totalDiscount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public SimpleObjectProperty<OrderStatus> orderStatusProperty() {
        return new SimpleObjectProperty<>(orderStatus);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus=orderStatus;
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
