package sample.repository.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import sample.enums.OrderStatus;
import sample.enums.OrderType;
import sample.model.Order;
import sample.model.UserSession;
import sample.repository.OrderDao;
import sample.service.OrderProductService;
import sample.utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.stream.Stream;

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
                    order.setDate(resultSet.getTimestamp("insert_date").toLocalDateTime());
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
        String sql = "INSERT INTO order_details (customer_name,customer_address,description,order_type,order_status,price_total,insert_date,updated_by,is_active)" +
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
                ps.setInt(8, 1);
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
        String sql="UPDATE order_details set customer_name=?,customer_address=?,description=?,order_type=?,order_status=?,price_total=?,insert_date=?,updated_by=? where id=?";
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
        String sql="Update order_details set is_active=0 where id=?";
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
                    order.setOrderType(OrderType.valueOf(resultSet.getString("order_type")));
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
        Stream<ObservableList> stream=Stream.of(searchResult);
        stream.distinct().limit(5);
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
           logger.error(e+" "+userSession.getUser().toString());
           e.printStackTrace();
        }
        return count;
    }
}
