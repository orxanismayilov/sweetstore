package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.repository.impl.OrderProductImpl;
import sample.service.OrderProductService;

import java.math.BigDecimal;
import java.sql.SQLException;

public class OrderProductSummary {

    private String description;
    private BigDecimal sum;
    private BigDecimal totalDiscount;
    private OrderProductService orderProductService;

    public OrderProductSummary() {
        orderProductService=new OrderProductService(new OrderProductImpl());
    }
    public void  fillDescriptionCalculateTotalPriceAndDiscount(int orderId){
        ObservableList<OrderProduct> list;
        list=orderProductService.getOrderProductByOrderId(orderId);
        StringBuilder descriptionBuilder=new StringBuilder();
        this.description="";
        this.sum=new BigDecimal("0");
        this.totalDiscount=new BigDecimal("0");
        for(OrderProduct orderProduct:list){
            sum=sum.add(orderProduct.getTotalPrice());
            totalDiscount=totalDiscount.add(BigDecimal.valueOf(orderProduct.getDiscount()));
            descriptionBuilder.append(orderProduct.getDescription()+",");
        }
        int index = descriptionBuilder.lastIndexOf(",");
       if (index>0) {
           description = String.valueOf(descriptionBuilder.substring(0, index - 1));
       } else {
           description=String.valueOf(descriptionBuilder);
       }
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getSum() {
        sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
        return sum;
    }

    public BigDecimal getTotalDiscount() {
        totalDiscount = totalDiscount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return totalDiscount;
    }
}
