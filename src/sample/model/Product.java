package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import sun.util.resources.LocaleData;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Product {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty quantity;
    private LocalDate updateDate;
    private SimpleObjectProperty<BigDecimal> price;
    private boolean isActive;

    public Product() {
        this.id=new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty("");
        this.quantity = new SimpleIntegerProperty(0);
        this.price = new SimpleObjectProperty<BigDecimal>(new BigDecimal(0));
        this.updateDate=LocalDate.now();
        this.isActive=true;
    }

    public Product(int id, String name, int quantity, BigDecimal price, LocalDate updateDate) {
        this.id=new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleObjectProperty<>(price);
        this.updateDate = updateDate;
        this.isActive=true;
    }
    public Product( String name, int quantity, BigDecimal price, LocalDate updateDate) {
        this.id=new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleObjectProperty<>(price);
        this.updateDate = updateDate;
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

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public SimpleObjectProperty<BigDecimal> priceProperty() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price.set(price);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}