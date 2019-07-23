package model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Product {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty quantity;
    private SimpleObjectProperty<LocalDateTime> updateDate;
    private SimpleFloatProperty price;
    private boolean isActive;

    public Product() {
        this.id = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty("");
        this.quantity = new SimpleIntegerProperty(0);
        this.price = new SimpleFloatProperty(0);
        this.updateDate = new SimpleObjectProperty<>(LocalDateTime.now());
        this.isActive = true;
    }

    public Product(int id, String name, int quantity, float price, LocalDateTime updateDate) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleFloatProperty(price);
        this.updateDate = new SimpleObjectProperty<>(updateDate);
        this.isActive = true;
    }

    public Product(String name, int quantity, float price, LocalDateTime updateDate) {
        this.id = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleFloatProperty(price);
        this.updateDate = new SimpleObjectProperty<>(updateDate);
        this.isActive = true;
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

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public LocalDateTime getUpdateDate() {
        return updateDate.get();
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate.set(updateDate);
    }

    public SimpleObjectProperty<LocalDateTime> updateDateProperty() {
        return updateDate;
    }

    public float getPrice() {
        return price.get();
    }

    public SimpleFloatProperty priceProperty() {
        return price;
    }

    public void setPrice(float price) {
        this.price.set(price);
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