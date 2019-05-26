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
    private static Logger logger = Logger.getLogger(OrderProductImpl.class.getName());

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
            logger.error(e);
        }
        return orderProductsList;
    }

    @Override
    public void saveOrderProduct(OrderProduct orderProduct) {
        String sqlOrderProduct = "INSERT INTO order_product (order_Id,product_Id,price,quantity,total_price,discount,description,is_active)" +
                "VALUES (?,?,?,?,?,?,?,?)";
        String sqlProduct = "Update products set quantity=quantity-? where id=?";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psOrderProduct = con.prepareStatement(sqlOrderProduct);
                 PreparedStatement psProduct=con.prepareStatement(sqlProduct)) {
                psOrderProduct.setInt(1, orderProduct.getOrderId());
                psOrderProduct.setInt(2, orderProduct.getProductId());
                psOrderProduct.setFloat(3, orderProduct.getProductPrice());
                psOrderProduct.setInt(4, orderProduct.getProductQuantity());
                psOrderProduct.setFloat(5, orderProduct.getTotalPrice().floatValue());
                psOrderProduct.setFloat(6, orderProduct.getDiscount());
                psOrderProduct.setString(7, orderProduct.getDescription());
                psOrderProduct.setInt(8, orderProduct.isActive() ? 1 : 0);
                psOrderProduct.executeUpdate();

                psProduct.setInt(1,orderProduct.getProductQuantity());
                psProduct.setInt(2,orderProduct.getProductId());
                psProduct.executeUpdate();

            } catch (SQLException e) {
                con.rollback();
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    public void removeOrderProductByOrderId(OrderProduct orderProduct,int orderId) {

    }

    @Override
    public void removeOrderProductById(OrderProduct orderProduct,int id) {
        String sqlOrderProduct="UPDATE order_product set is_active=0 where id=?";
        String sqlProduct="UPDATE products set quantity=quantity+? where id=?";
        try (Connection con=DBConnection.getConnection())
        {
            con.setAutoCommit(false);
            try (PreparedStatement ps=con.prepareStatement(sqlOrderProduct);
                 PreparedStatement psProduct=con.prepareStatement(sqlProduct))
            {
                ps.setInt(1,id);
                ps.executeUpdate();

                psProduct.setInt(1,orderProduct.getProductQuantity());
                psProduct.setInt(2,orderProduct.getProductId());
                psProduct.executeUpdate();
            } catch (SQLException e) {
                con.rollback();
                e.printStackTrace();
            }
           con.commit();
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
        String sqlProduct = "Update products set quantity=quantity-? where id=?";
        try (Connection con = DBConnection.getConnection()) {
           con.setAutoCommit(false);
            try (PreparedStatement psOrderProduct = con.prepareStatement(sqlOrderProduct);
                 PreparedStatement psProduct = con.prepareStatement(sqlProduct)) {
                psOrderProduct.setInt(1, newOrderProduct.getProductQuantity());
                psOrderProduct.setFloat(2, newOrderProduct.getDiscount());
                psOrderProduct.setFloat(3,newOrderProduct.getTotalPrice().floatValue());
                psOrderProduct.setString(4, newOrderProduct.getDescription());
                psOrderProduct.setInt(5, id);
                psOrderProduct.executeUpdate();

                psProduct.setInt(1, newOrderProduct.getProductQuantity());
                psProduct.setInt(2, newOrderProduct.getProductId());
                psProduct.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                con.rollback();
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
}
