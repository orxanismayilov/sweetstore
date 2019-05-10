package sample.repository.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.enums.OrderStatus;
import sample.enums.OrderType;
import sample.model.Order;
import sample.model.UserSession;
import sample.repository.OrderDao;
import sample.utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public class OrderDaoImpl implements OrderDao {
    private static Logger logger=Logger.getLogger(OrderDaoImpl.class.getName());
    private UserSession userSession;

    public OrderDaoImpl() {
        userSession=UserSession.getInstance();
    }

    @Override
    public ObservableList getOrderList(int pageIndex, int rowsPerPage) {
        String sql="select * from order_details where is_active=1  ORDER by id desc Limit ?,?";
        ObservableList<Order> orderList= FXCollections.observableArrayList();
        int totalCount=getTotalCountOfOrder();
        int fromIndex=pageIndex*rowsPerPage;
        int toIndex=Math.min(fromIndex+rowsPerPage,totalCount);
        try (Connection conn=DBConnection.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql))
        {
            ps.setInt(1,fromIndex);
            ps.setInt(2,toIndex);

            try (ResultSet resultSet=ps.executeQuery()){
                while (resultSet.next()) {
                    Order order=new Order();
                    order.setTransactionID(resultSet.getInt("id"));
                    order.setCustomerName(resultSet.getString("customer_name"));
                    order.setCustomerAddress(resultSet.getString("customer_address"));
                    order.setDescription(resultSet.getString("description"));
                    order.setOrderType(OrderType.valueOf(resultSet.getString("order_type")));
                    order.setTotalPrice(new BigDecimal(String.valueOf(resultSet.getFloat("price_total"))));
                    order.setOrderStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
                    order.setDate(LocalDateTime.now());
                    order.setActive(true);
                    orderList.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public void addOrder(Order order) {
        String sql="INSERT INTO products (customer_name,customer_address,description,order_type,order_status,price_total,insert_date,updated_by,is_active)" +
                " VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql))
        {
         ps.setString(1,order.getCustomerName());
         ps.setString(2,order.getCustomerAddress());
         ps.setString(3,order.getDescription());
         ps.setString(4,order.getOrderType().getEngMeaning());
         ps.setString(5,order.getOrderStatus().getEngMeaning());
         ps.setFloat(6,order.getTotalPrice().floatValue());
         ps.setDate(7,Date.valueOf("2015-03-31"));
         ps.setString(8,userSession.getUserName());
         ps.setInt(9,1);
         ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateOrder(Order newOrder, int oldOrderId) {
        String sql="UPDATE order_details set customer_name=?,customer_address=?,description=?,order_type=?,order_status=?,price_total=?,insert_date=?,updated_by=? where id=?";
        try (Connection con=DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql))
        {
            ps.setString(1,newOrder.getCustomerName());
            ps.setString(2,newOrder.getCustomerAddress());
            ps.setString(3,newOrder.getDescription());
            ps.setString(4,newOrder.getOrderType().getEngMeaning());
            ps.setString(5,newOrder.getOrderStatus().getEngMeaning());
            ps.setFloat(6,newOrder.getTotalPrice().floatValue());
            ps.setDate(7,Date.valueOf("2015-03-31"));
            ps.setString(8,userSession.getUserName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrderByTransactionId(int transactionId) {
        String sql="Update order_details set is_active=1 where id=?";
        try (Connection connection=DBConnection.getConnection();
             PreparedStatement ps=connection.prepareStatement(sql))
        {
        ps.setInt(1,transactionId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Order getOrderById(int orderId) {
        String sql="SELECT * from where id=?";
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql))
        {
            ps.setInt(1,orderId);
            try(ResultSet rs=ps.executeQuery()) {
                while (rs.next()) {
                   Order order=new Order();
                   order.setTransactionID(rs.getInt("id"));
                   order.setCustomerName(rs.getString("customer_name"));
                   order.setCustomerAddress(rs.getString("customer_address"));
                   order.setDescription(rs.getString("description"));
                   order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
                   order.setOrderType(OrderType.valueOf(rs.getString("order_type")));
                   order.setTotalPrice(new BigDecimal(String.valueOf(rs.getFloat("price_total"))));
                   order.setDate(LocalDateTime.now());
                   order.setTotalDiscount(new BigDecimal(String.valueOf(rs.getFloat("discount"))));
                   order.setActive(true);
                   return order;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ObservableList searchOrderById(String id) {
        ObservableList<Order> searchResult=FXCollections.observableArrayList();
        String sc="%"+id+"%";
        String sql="SELECT * FROM order_details WHERE id LIKE ? ";
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql))
        {
            ps.setString(1,sc);
            try (ResultSet resultSet=ps.executeQuery()) {
                while (resultSet.next()) {
                    Order order=new Order();
                    order.setTransactionID(resultSet.getInt("id"));
                    order.setCustomerName(resultSet.getString("customer_name"));
                    order.setCustomerAddress(resultSet.getString("customer_address"));
                    order.setDescription(resultSet.getString("description"));
                    order.setOrderType(OrderType.valueOf(resultSet.getString("oreder_type")));
                    order.setTotalPrice(new BigDecimal(String.valueOf(resultSet.getFloat("price_total"))));
                    order.setOrderStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
                    order.setDate(LocalDateTime.now());
                    order.setActive(true);
                    searchResult.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    @Override
    public int getTotalCountOfOrder() {
        String sql="SELECT * FROM order_details where is_active=1";
        int count =0;
        try (Connection conn= DBConnection.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql);
             ResultSet rs=ps.executeQuery();)
        {
            while (rs.next()) {
               count++;
            }
        } catch (SQLException e) {
           logger.error(e+" "+userSession.getUserName());
           e.printStackTrace();
        }
        return count;
    }
}