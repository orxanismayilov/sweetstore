package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
    ObservableList<Order> data=FXCollections.observableArrayList(
            new Order("Orxan","Mallar",15,"Online",123),
            new Order("Amil","sirniyyat",23,"offline",456),
            new Order("eltun","guller",456,"dsfjds;",132)
    );
    public ObservableList getData(){
        return data;
    }
    public void addData(Order o){
        data.add(o);
    }
    public void deleteData(Integer id){
      data.removeIf(order -> order.getTrasactionid().equals(id));
    }

}
