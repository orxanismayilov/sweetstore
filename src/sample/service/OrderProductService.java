package sample.service;

import javafx.collections.ObservableList;
import sample.model.OrderProduct;
import sample.repository.OrderProductDummyRepo;

public class OrderProductService {
    private OrderProductDummyRepo orderProductDummyRepo;

    public OrderProductService() {
        this.orderProductDummyRepo = new OrderProductDummyRepo();
    }

    public ObservableList getOrderProductList(){
        return orderProductDummyRepo.getList();
    }

    public void addOrderProductToList(OrderProduct orderProduct){
        orderProduct.setId(orderProductDummyRepo.getOrderProductNewId());
        orderProductDummyRepo.addOrderProductToList(orderProduct);
    }

    public void deletOrderProductByOrderId(int orderId){
        orderProductDummyRepo.removeOrderProductByOrderId(orderId);
    }

    public void removeOrderProductByProductId(int productId){
        orderProductDummyRepo.removeOrderProductById(productId);
    }

    public ObservableList getOrderProductByOrderId(int orderId){
        return orderProductDummyRepo.getOrderProductByOrderId(orderId);
    }
}
