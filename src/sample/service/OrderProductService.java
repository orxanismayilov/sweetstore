package sample.service;

import javafx.collections.ObservableList;
import sample.model.OrderProduct;
import sample.repository.OrderProductDummyRepo;
import sample.utils.Notification;

import java.math.BigDecimal;

public class OrderProductService {
    private OrderProductDummyRepo orderProductDummyRepo;
    private StringBuilder DESCRIPTION_TEXT=new StringBuilder();
    private BigDecimal sum;
    private BigDecimal totalDiscount=new BigDecimal(0);

    public OrderProductService() {
        this.orderProductDummyRepo = new OrderProductDummyRepo();
    }

    public ObservableList getOrderProductList(){
        return orderProductDummyRepo.getList();
    }

    public void addOrderProducttoList(OrderProduct orderProduct){
        orderProduct.setId(orderProductDummyRepo.getOrderProductNewId());
        orderProductDummyRepo.addOrderProducttoList(orderProduct);
    }

    public void deletOrderProductbyOrderId(int orderId){
        orderProductDummyRepo.removeOrderProductbyOrderId(orderId);
    }

    public void removeOrderProductbyProductId(int productId){
        orderProductDummyRepo.removeOrderProductbyId(productId);
    }

    private void validateOrderProduct(OrderProduct orderProduct){
        Notification errors=new Notification();
        if(orderProduct.getProductQuantity()<0 || orderProduct.getProductQuantity()>1000 ) errors.addError("");
        if (orderProduct.getDiscount()<0 || orderProduct.getDiscount()>orderProduct.getTotalPrice().doubleValue()) errors.addError("");

    }

    public StringBuilder fillDescription(){
        ObservableList<OrderProduct> list=getOrderProductList();
        DESCRIPTION_TEXT=new StringBuilder();
        for(OrderProduct orderProduct:list){
            DESCRIPTION_TEXT.append(orderProduct.getDescription());
        }
        return DESCRIPTION_TEXT;
    }

    public ObservableList getOrderProductbyOrderId(int orderId){
        return orderProductDummyRepo.getOrderProductbyOrderId(orderId);
    }
}
