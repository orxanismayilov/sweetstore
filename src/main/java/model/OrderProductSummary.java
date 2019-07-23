package model;

import javafx.collections.ObservableList;
import repository.impl.OrderProductImpl;
import service.serviceImpl.OrderProductServiceImpl;

import java.math.BigDecimal;

public class OrderProductSummary {

    private String description;
    private BigDecimal sum;
    private BigDecimal totalDiscount;
    private OrderProductServiceImpl orderProductServiceImpl;

    public OrderProductSummary() {
        orderProductServiceImpl =new OrderProductServiceImpl(new OrderProductImpl());
    }
    public void  fillDescriptionCalculateTotalPriceAndDiscount(int orderId){
        ObservableList<OrderProduct> list;
        list= orderProductServiceImpl.getOrderProductByOrderId(orderId);
        StringBuilder descriptionBuilder=new StringBuilder();
        this.description="";
        this.sum=new BigDecimal("0");
        this.totalDiscount=new BigDecimal("0");
        for(OrderProduct orderProduct:list){
            sum=sum.add(orderProduct.getTotalPrice());
            totalDiscount=totalDiscount.add(BigDecimal.valueOf(orderProduct.getDiscount()));
            descriptionBuilder.append(orderProduct.getDescription()).append(",");
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
