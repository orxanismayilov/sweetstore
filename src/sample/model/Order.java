package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {
    private SimpleStringProperty names;
    private SimpleStringProperty address;
    private SimpleStringProperty description;
    private SimpleIntegerProperty totalprice;
    private SimpleStringProperty ordertype;
    private SimpleIntegerProperty trasactionid;
    private LocalDate date;

    public Order(String names, String description, Integer totalprice, String ordertype, Integer trasactionid,LocalDate date) {
        this.names = new SimpleStringProperty(names);
        this.description = new SimpleStringProperty(description);
        this.totalprice = new SimpleIntegerProperty(totalprice);
        this.ordertype = new SimpleStringProperty(ordertype);
        this.trasactionid = new SimpleIntegerProperty(trasactionid);
        this.date=date;
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public SimpleStringProperty namesProperty() {
        return names;
    }

    public void setName(String name) {
        this.names.set(name);
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public SimpleIntegerProperty totalpriceProperty() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice.set(totalprice);
    }

    public SimpleStringProperty ordertypeProperty() {
        return ordertype;
    }


    public void setOrdertype(String ordertype) {
        this.ordertype.set(ordertype);
    }

    public SimpleIntegerProperty trasactionidProperty() {
        return trasactionid;
    }

    public int getTotalPrice(){
        return totalprice.get();
    }

    /*
    public void setTrasactionid(int trasactionid) {
        this.trasactionid.set(trasactionid);
    }

    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }

    public Integer getTotalprice() {
        return totalprice.get();
    }

    public String getOrdertype() {
        return ordertype.get();
    }

    public Integer getTrasactionid() {
        return trasactionid.get();
    }*/
}
