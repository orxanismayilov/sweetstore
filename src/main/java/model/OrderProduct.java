package model;

import javafx.beans.property.*;

import java.math.BigDecimal;

public class OrderProduct {

    private int id;
    private int orderId;
    private int productId;
    private String productName;
    private int productQuantity;
    private float productPrice;
    private BigDecimal totalPrice;
    private float discount;
    private String description;
    private boolean isActive;

    public OrderProduct() {
        this.id=0;
        this.orderId=0;
        this.productId=0;
        this.productName="";
        this.productQuantity=0;
        this.productPrice=0;
        this.totalPrice=new BigDecimal("0");
        this.discount=0;
        this.description="";
        this.isActive=true;
    }

    public int getId() {
        return id;
    }

    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getOrderId() {
        return orderId;
    }

    public SimpleIntegerProperty orderIdProperty() {
        return new SimpleIntegerProperty(orderId);
    }

    public void setOrderId(int orderId) {
        this.orderId=orderId;
    }

    public int getProductId() {
        return productId=productId;
    }

    public SimpleIntegerProperty productIdProperty() {
        return new SimpleIntegerProperty(productId);
    }

    public void setProductId(int productId) {
        this.productId=productId;
    }

    public String getProductName() {
        return productName;
    }

    public SimpleStringProperty productNameProperty() {
        return new SimpleStringProperty(productName);
    }

    public void setProductName(String productName) {
        this.productName=productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public SimpleIntegerProperty productQuantityProperty() {
        return new SimpleIntegerProperty(productQuantity);
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity=productQuantity;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public SimpleFloatProperty productPriceProperty() {
        return new SimpleFloatProperty(productPrice);
    }

    public void setProductPrice(float productPrice) {
        this.productPrice=productPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public SimpleObjectProperty<BigDecimal> totalPriceProperty() {
        return new SimpleObjectProperty<BigDecimal>(totalPrice) ;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice=totalPrice;
    }

    public float getDiscount() {
        return discount;
    }

    public SimpleFloatProperty discountProperty() {
        return new SimpleFloatProperty(discount);
    }

    public void setDiscount(float discount) {
        this.discount=discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
