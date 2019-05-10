package sample.repository.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.model.OrderProduct;
import sample.repository.OrderProductDao;
import sample.utils.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderProductImpl implements OrderProductDao {
    private static Logger logger=Logger.getLogger(OrderProductImpl.class.getName());

    @Override
    public ObservableList<OrderProduct> getList() {
        ObservableList<OrderProduct> orderProductsList= FXCollections.observableArrayList();
        String sql="SELECT * FROM order_product where is_active=1";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql);
             ResultSet rs=ps.executeQuery())
        {
            while (rs.next()) {
                OrderProduct orderProduct=new OrderProduct();
                orderProduct.setId(rs.getInt("id"));
                orderProduct.setOrderId(rs.getInt("order_id"));
                orderProduct.setProductId(rs.getInt("product_id"));
                orderProduct.setProductPrice(rs.getFloat("price"));
                orderProduct.setProductQuantity(rs.getInt("quantity"));
                orderProduct.setTotalPrice(new BigDecimal(String.valueOf(rs.getFloat("total_price"))));
                orderProduct.setDiscount(rs.getFloat("discount"));
                orderProduct.setDescription(rs.getString("description"));
                orderProduct.setActive(true);
                orderProductsList.add(orderProduct);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return null;
    }

    @Override
    public void addOrderProductToList(OrderProduct orderProduct) {

    }

    @Override
    public void removeOrderProductByOrderId(int orderId) {

    }

    @Override
    public void removeOrderProductById(int id) {

    }

    @Override
    public OrderProduct doesOrderProductExist(OrderProduct newOrderProduct) {
        return null;
    }

    @Override
    public void updateOrderProduct(OrderProduct newOrderProduct, int id) {

    }

    @Override
    public ObservableList getOrderProductByOrderId(int orderId) {
        return null;
    }
}
