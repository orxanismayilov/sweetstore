package sample.repository.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.model.Order;
import sample.model.OrderProduct;
import sample.repository.OrderProductDao;
import sample.utils.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderProductImpl implements OrderProductDao {
    private static Logger logger = Logger.getLogger(OrderProductImpl.class);

    @Override
    public ObservableList<OrderProduct> getListByOrderId(int orderId) {
        ObservableList<OrderProduct> orderProductsList = FXCollections.observableArrayList();
        String sql = "select \n" +
                "order_product.id,order_product.order_Id,order_product.product_id,products.name,order_product.price,order_product.quantity,\n" +
                "order_product.total_price,order_product.discount,order_product.description\n" +
                "from \n" +
                "order_product  \n" +
                "inner join products  on order_product.product_id=products.id \n" +
                "where order_id=? and order_product.is_active=1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setId(rs.getInt("id"));
                    orderProduct.setOrderId(rs.getInt("order_id"));
                    orderProduct.setProductId(rs.getInt("product_id"));
                    orderProduct.setProductName(rs.getString("name"));
                    orderProduct.setProductPrice(rs.getFloat("price"));
                    orderProduct.setProductQuantity(rs.getInt("quantity"));
                    orderProduct.setTotalPrice(new BigDecimal(String.valueOf(rs.getFloat("total_price"))));
                    orderProduct.setDiscount(rs.getFloat("discount"));
                    orderProduct.setDescription(rs.getString("description"));
                    orderProduct.setActive(true);
                    orderProductsList.add(orderProduct);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderProductsList;
    }

    @Override
    public void saveOrderProduct(OrderProduct orderProduct) {
        String sqlOrderProduct = "INSERT INTO order_product (order_Id,product_Id,price,quantity,total_price,discount,description,is_active)" +
                "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement psOrderProduct = con.prepareStatement(sqlOrderProduct)) {
            psOrderProduct.setInt(1, orderProduct.getOrderId());
            psOrderProduct.setInt(2, orderProduct.getProductId());
            psOrderProduct.setFloat(3, orderProduct.getProductPrice());
            psOrderProduct.setInt(4, orderProduct.getProductQuantity());
            psOrderProduct.setFloat(5, orderProduct.getTotalPrice().floatValue());
            psOrderProduct.setFloat(6, orderProduct.getDiscount());
            psOrderProduct.setString(7, orderProduct.getDescription());
            psOrderProduct.setInt(8, orderProduct.isActive() ? 1 : 0);
            psOrderProduct.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeOrderProductByOrderId(OrderProduct orderProduct,int orderId) {

    }

    @Override
    public void removeOrderProductById(OrderProduct orderProduct, int id) {
        String sqlOrderProduct = "UPDATE order_product set is_active=0 where id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlOrderProduct)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrderProduct doesOrderProductExist(OrderProduct newOrderProduct) {
        return null;
    }

    @Override
    public void updateOrderProduct(OrderProduct newOrderProduct, int id) {
        String sqlOrderProduct = "UPDATE order_product set quantity=?,discount=?,total_price=?,description=? where Id = ? ";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement psOrderProduct = con.prepareStatement(sqlOrderProduct)) {
            psOrderProduct.setInt(1, newOrderProduct.getProductQuantity());
            psOrderProduct.setFloat(2, newOrderProduct.getDiscount());
            psOrderProduct.setFloat(3, newOrderProduct.getTotalPrice().floatValue());
            psOrderProduct.setString(4, newOrderProduct.getDescription());
            psOrderProduct.setInt(5, id);
            psOrderProduct.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
