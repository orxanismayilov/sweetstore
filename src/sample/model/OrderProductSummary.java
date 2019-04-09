package sample.model;

import javafx.collections.ObservableList;
import sample.service.OrderProductService;

import java.math.BigDecimal;

public class OrderProductSummary {

    private String description;
    private BigDecimal sum;
    private BigDecimal totalDiscount;
    private OrderProductService orderProductService;

    public OrderProductSummary() {
        orderProductService=new OrderProductService();
    }
    public void  fillDescriptionCalculateTotalPriceAndDiscount(int orderId){
        ObservableList<OrderProduct> list=orderProductService.getOrderProductByOrderId(orderId);
        StringBuilder descriptionBuilder=new StringBuilder();
        this.description="";
        this.sum=new BigDecimal("0");
        this.totalDiscount=new BigDecimal("0");
        for(OrderProduct orderProduct:list){
            sum=sum.add(orderProduct.getTotalPrice());
            totalDiscount=totalDiscount.add(BigDecimal.valueOf(orderProduct.getDiscount()));
            descriptionBuilder.append(orderProduct.getDescription()+" ");
        }
        description= String.valueOf(descriptionBuilder);
    }

    public String getDescription() {
        totalDiscount = totalDiscount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return description;
    }

    public BigDecimal getSum() {
        sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
        return sum;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }
}
