package dtos;

import model.Order;

import java.util.List;

public class OrdersDTO {
    private int count;
    private List<Order> orders;

    public OrdersDTO() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
