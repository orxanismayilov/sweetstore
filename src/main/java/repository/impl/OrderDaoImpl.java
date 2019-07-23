package repository.impl;

import enums.OrderStatus;
import enums.OrderType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Order;
import model.UserSession;
import repository.OrderDao;
import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private UserSession userSession;

    public OrderDaoImpl() {
        userSession=UserSession.getInstance();
    }

    @Override
    public ObservableList getOrderList(int pageIndex, int rowsPerPage) {
        String sql="select * from ORDER_DETAILS where is_active=1  ORDER by id desc Limit ?,?";
        ObservableList<Order> orderList= FXCollections.observableArrayList();
        int totalCount=getTotalCountOfOrder();
        int fromIndex=pageIndex*rowsPerPage;
        int toIndex=Math.min(fromIndex+rowsPerPage,totalCount);
        try (Connection conn= DBConnection.getConnection();
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
                    if (resultSet.getTimestamp("insert_date")!= null) {
                        order.setDate(resultSet.getTimestamp("insert_date").toLocalDateTime());
                    }
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
    public int addOrder(Order order) {

        //master
        String sql = "INSERT INTO ORDER_DETAILS (customer_name,customer_address,description,order_type,order_status,price_total,insert_date,updated_by,is_active)" +
                " VALUES (?,?,?,?,?,?,?,?,?)";
        Timestamp ts=Timestamp.valueOf(LocalDateTime.now());
        try (Connection con = DBConnection.getConnection()
        ) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, order.getCustomerName());
                ps.setString(2, order.getCustomerAddress());
                ps.setString(3, order.getDescription());
                ps.setString(4, order.getOrderType().toString());
                ps.setString(5, OrderStatus.CLOSED.toString());
                ps.setFloat(6, order.getTotalPrice().floatValue());
                ps.setTimestamp(7,ts);
                ps.setInt(8, userSession.getUser().getId());
                ps.setInt(9, 1);

                int rowAffected = ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    int orderId = 0;
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }

                    if (rowAffected == 1) {
                        con.commit();
                        return orderId;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    @Override
    public void updateOrder(Order newOrder, int oldOrderId) {
        String sql="UPDATE ORDER_DETAILS set customer_name=?,customer_address=?,description=?,order_type=?,order_status=?,price_total=?,insert_date=?,updated_by=? where id=?";
        Timestamp ts=Timestamp.valueOf(LocalDateTime.now());
        try (Connection con=DBConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(sql))
        {
            ps.setString(1,newOrder.getCustomerName());
            ps.setString(2,newOrder.getCustomerAddress());
            ps.setString(3,newOrder.getDescription());
            ps.setString(4,newOrder.getOrderType().toString());
            ps.setString(5,newOrder.getOrderStatus().toString());
            ps.setFloat(6,newOrder.getTotalPrice().floatValue());
            ps.setTimestamp(7,ts);
            ps.setInt(8,1);
            ps.setInt(9,oldOrderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrderByTransactionId(int transactionId) {
        String sql="Update ORDER_DETAILS set is_active=0 where id=?";
        try (Connection connection=DBConnection.getConnection();
             PreparedStatement ps=connection.prepareStatement(sql))
        {
        ps.setInt(1,transactionId);
        ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Order> searchOrderById(String id,boolean searchAll) {
        List<Order> searchResult=new ArrayList<>();
        String sc="%"+id+"%";
        String sql="SELECT * FROM ORDER_DETAILS WHERE id LIKE ? and is_active=?";
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql))
        {
            ps.setString(1,sc);
            ps.setInt(2,searchAll ? 0:1);
            try (ResultSet resultSet=ps.executeQuery()) {
                while (resultSet.next()) {
                    Order order=new Order();
                    order.setTransactionID(resultSet.getInt("id"));
                    order.setCustomerName(resultSet.getString("customer_name"));
                    order.setCustomerAddress(resultSet.getString("customer_address"));
                    order.setDescription(resultSet.getString("description"));
                    order.setOrderType(OrderType.valueOf(resultSet.getString("order_type")));
                    order.setTotalPrice(new BigDecimal(String.valueOf(resultSet.getFloat("price_total"))));
                    order.setOrderStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
                    if (resultSet.getTimestamp("insert_date")!=null){
                        order.setDate(resultSet.getTimestamp("insert_date").toLocalDateTime());
                    }
                    order.setActive(resultSet.getBoolean("is_active"));
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
        String sql="SELECT * FROM ORDER_DETAILS where is_active=1";
        int count =0;
        try (Connection conn= DBConnection.getConnection();
             PreparedStatement ps=conn.prepareStatement(sql);
             ResultSet rs=ps.executeQuery();)
        {
            while (rs.next()) {
               count++;
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return count;
    }
}