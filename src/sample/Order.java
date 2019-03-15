package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order {
    private SimpleStringProperty name;
    private SimpleStringProperty description;
    private SimpleIntegerProperty totalprice;
    private SimpleStringProperty ordertype;
    private SimpleIntegerProperty trasactionid;

    public Order(String name, String description, Integer totalprice, String ordertype, Integer trasactionid) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.totalprice = new SimpleIntegerProperty(totalprice);
        this.ordertype = new SimpleStringProperty(ordertype);
        this.trasactionid = new SimpleIntegerProperty(trasactionid);
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
    }
}
