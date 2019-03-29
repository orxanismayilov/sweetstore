package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

public class OrderProduct {

    private SimpleIntegerProperty orderId;
    private SimpleIntegerProperty productId;
    private SimpleStringProperty productName;
    private SimpleIntegerProperty productQuantity;
    private BigDecimal productPrice;
    private BigDecimal totalPrice;
    private Double discount;

    public OrderProduct() {
        this.orderId=new SimpleIntegerProperty(0);
        this.productId=new SimpleIntegerProperty(0);
        this.productName=new SimpleStringProperty("");
        this.productQuantity=new SimpleIntegerProperty(0);
        this.productPrice=new BigDecimal(0);
        this.totalPrice=new BigDecimal(0);
        this.discount=0.0;
    }

    public int getOrderId() {
        return orderId.get();
    }

    public SimpleIntegerProperty orderIdProperty() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId.set(orderId);
    }

    public int getProductId() {
        return productId.get();
    }

    public SimpleIntegerProperty productIdProperty() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public int getProductQuantity() {
        return productQuantity.get();
    }

    public SimpleIntegerProperty productQuantityProperty() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity.set(productQuantity);
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
