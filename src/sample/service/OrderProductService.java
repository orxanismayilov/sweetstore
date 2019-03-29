package sample.service;

import javafx.collections.ObservableList;
import sample.model.OrderProduct;
import sample.repository.OrderProductDummyRepo;

public class OrderProductService {
    OrderProductDummyRepo orderProductDummyRepo;

    public OrderProductService() {
        this.orderProductDummyRepo = new OrderProductDummyRepo();
    }

    public ObservableList getOrderProductList(){
        return orderProductDummyRepo.getList();
    }

    public void addOrderProducttoList(OrderProduct orderProduct){
        orderProductDummyRepo.addOrderProducttoList(orderProduct);
    }

    public void deletOrderProductbyOrderId(int orderId){
        orderProductDummyRepo.removeOrderPrductbyOrderId(orderId);
    }

}
