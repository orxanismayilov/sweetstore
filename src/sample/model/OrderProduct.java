package sample.model;

import javafx.beans.property.*;

import java.math.BigDecimal;

public class OrderProduct {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty orderId;
    private SimpleIntegerProperty productId;
    private SimpleStringProperty productName;
    private SimpleIntegerProperty productQuantity;
    private SimpleFloatProperty productPrice;
    private SimpleObjectProperty<BigDecimal> totalPrice;
    private SimpleFloatProperty discount;
    private String description;
    private boolean isActive;

    public OrderProduct() {
        this.id=new SimpleIntegerProperty(0);
        this.orderId=new SimpleIntegerProperty(0);
        this.productId=new SimpleIntegerProperty(0);
        this.productName=new SimpleStringProperty("");
        this.productQuantity=new SimpleIntegerProperty(0);
        this.productPrice=new SimpleFloatProperty(0);
        this.totalPrice=new SimpleObjectProperty<>(new BigDecimal(0));
        this.discount=new SimpleFloatProperty(0);
        this.isActive=true;

    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
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

    public double getProductPrice() {
        return productPrice.get();
    }

    public SimpleFloatProperty productPriceProperty() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice.set(productPrice);
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

    public float getDiscount() {
        return discount.get();
    }

    public SimpleFloatProperty discountProperty() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount.set(discount);
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
