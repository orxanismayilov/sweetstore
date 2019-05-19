package sample.repository.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.model.OrderProduct;
import sample.repository.OrderProductDao;
import sample.service.ProductService;
import sample.utils.CopyListUtil;
import sample.utils.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderProductImpl implements OrderProductDao {
    private static Logger logger=Logger.getLogger(OrderProductImpl.class.getName());

    @Override
    public ObservableList<OrderProduct> getListByOrderId(int orderId) {
        ObservableList<OrderProduct> orderProductsList= FXCollections.observableArrayList();
        String sql="select \n" +
                "order_product.id,order_product.order_Id,order_product.product_id,products.name,order_product.price,order_product.quantity,\n" +
                "order_product.total_price,order_product.discount,order_product.description\n" +
                "from \n" +
                "order_product  \n" +
                "inner join products  on order_product.product_id=products.id \n" +
                "where order_id=?";
        String sqlProduct="Select * from products where id=?";
        try (Connection con= DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql))
        {
            ps.setInt(1,orderId);
            try (ResultSet rs=ps.executeQuery()){
                while (rs.next()) {
                    OrderProduct orderProduct=new OrderProduct();
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
            }catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            logger.error(e);
        }
        return orderProductsList;
    }

    @Override
    public void saveOrderProduct(OrderProduct orderProduct) {
        String sql = "INSERT INTO order_product (order_Id,product_Id,price,quantity,total_price,discount,description,is_active)" +
                "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderProduct.getOrderId());
            ps.setInt(2, orderProduct.getProductId());
            ps.setFloat(3, orderProduct.getProductPrice());
            ps.setInt(4, orderProduct.getProductQuantity());
            ps.setFloat(5, orderProduct.getTotalPrice().floatValue());
            ps.setFloat(6, orderProduct.getDiscount());
            ps.setString(7, orderProduct.getDescription());
            ps.setInt(8, orderProduct.isActive() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
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
}
