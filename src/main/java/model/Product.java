package model;

import javafx.beans.property.*;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {
    private String id;
    private String name;
    private int quantity;
    private String updateDate;
    private float price;
    private boolean isActive;

    public Product() {
    }

    public Product(String id, String name, int quantity, String updateDate, float price, boolean isActive) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.updateDate = updateDate;
        this.price = price;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public SimpleStringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name=name;
    }

    public int getQuantity() {
        return quantity;
    }

    public SimpleIntegerProperty quantityProperty() {
        return new SimpleIntegerProperty(quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity=quantity;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate=updateDate;
    }

    public SimpleStringProperty updateDateProperty() {
        return new SimpleStringProperty(updateDate);
    }

    public float getPrice() {
        return price;
    }

    public SimpleFloatProperty priceProperty() {
        return new SimpleFloatProperty(price);
    }

    public void setPrice(float price) {
        this.price=price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name=" + name +
                ", quantity=" + quantity +
                ", updateDate=" + updateDate +
                ", price=" + price +
                '}';
    }
}