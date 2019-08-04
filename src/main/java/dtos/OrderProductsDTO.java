package dtos;

import model.OrderProduct;
import model.OrderProductSummary;

import java.util.List;

public class OrderProductsDTO {
    private List<OrderProduct> orderProducts;
    private OrderProductSummary summary;

    public OrderProductsDTO() {
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public OrderProductSummary getSummary() {
        return summary;
    }

    public void setSummary(OrderProductSummary summary) {
        this.summary = summary;
    }
}
