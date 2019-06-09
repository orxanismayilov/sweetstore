package sample.repository.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.OrderProduct;
import sample.model.Product;
import sample.repository.ProductDao;
import sample.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

public class ProductDaoImpl implements ProductDao {

    @Override
    public ObservableList getProductList(int pageIndex,int rowsPerPage) {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Products where is_active=1 ORDER BY id DESC Limit ?,? ";
        int totalCount= getTotalCountOfProduct();
        int fromIndex=pageIndex*rowsPerPage;
        int toIndex=Math.min(fromIndex+rowsPerPage,totalCount);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setInt(1, fromIndex);
            ps.setInt(2,toIndex);
           try (ResultSet rs=ps.executeQuery()){
               while (rs.next()) {
                   Product product = new Product();
                   product.setId(rs.getInt("Id"));
                   product.setName(rs.getString("Name"));
                   product.setQuantity(rs.getInt("Quantity"));
                   product.setPrice(rs.getFloat("Price"));
                  if (rs.getTimestamp("update_date")!=null) {
                      product.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
                  }
                   product.setActive(true);
                   productList.add(product);
               }
           } catch (SQLException e){
               e.printStackTrace();
           }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public ObservableList getProductListForComboBox() {
        String sql="select * from Products where Is_Active=1 and quantity>0";
        ObservableList productList=FXCollections.observableArrayList();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("Id"));
                product.setName(rs.getString("Name"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setPrice(rs.getFloat("Price"));
                if (rs.getTimestamp("update_date")!=null) {
                    product.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
                }
                product.setActive(true);

                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (Name,Quantity,Price,Update_Date,Is_Active) VALUES (?,?,?,?,?)";
        try(Connection connection=DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getQuantity());
            preparedStatement.setFloat(3, product.getPrice());
            preparedStatement.setDate(4, Date.valueOf("2015-03-31"));
            preparedStatement.setInt(5, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProductById(int id) {
        String sql = "UPDATE products set Is_Active =0,update_date =? where Id = ? ";
        try(Connection connection=DBConnection.getConnection();
            PreparedStatement ps=connection.prepareStatement(sql))
        {
            ps.setInt(2, id);
            ps.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product isProductExist(String name) {
        String s = "SELECT * from products  where Name = ? ";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps1 = connection.prepareStatement(s);
             PreparedStatement ps2 = connection.prepareStatement(s))
        {
            ps1.setString(1, name);
            ps2.setString(1, name);
            try (ResultSet rs1 = ps1.executeQuery();
                 ResultSet rs2 = ps2.executeQuery()) {
                if (rs1.next()) {
                    Product product = new Product();
                    while (rs2.next()) {
                        if (rs2.getInt("Is_Active") == 1) {
                            product.setId(rs2.getInt("Id"));
                            product.setName(rs2.getString("Name"));
                            product.setQuantity(rs2.getInt("Quantity"));
                            product.setPrice(rs2.getFloat("Price"));
                            product.setUpdateDate(LocalDateTime.now());
                            product.setActive(true);
                        }
                    }
                    return product;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateProductIncreaseQuantity(Product newProduct, int id) {
        String s = "UPDATE products set Quantity =Quantity+? , Price = ?,update_date=? where Id = ? ";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps=connection.prepareStatement(s))
        {
            ps.setInt(1, newProduct.getQuantity());
            ps.setFloat(2, newProduct.getPrice());
            ps.setInt(3,id);
            ps.setTimestamp(4,Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product, int oldProductId) {
        String sql = "UPDATE products set name=?,price=?,quantity=?,update_date=? where id=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
             ps.setString(1, product.getName());
             ps.setFloat(2, product.getPrice());
             ps.setInt(3, product.getQuantity());
             ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
             ps.setInt(5, oldProductId);
             ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product getProductById(int id) {
        String sql="SELECT * FROM  products WHERE id=? and is_active=1";
        try (Connection connection=DBConnection.getConnection();
             PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setInt(1,id);
            try (ResultSet rs=ps.executeQuery()){
                while (rs.next()) {
                    Product product=new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getFloat("price"));
                    product.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
                    product.setQuantity(rs.getInt("quantity"));
                    product.setActive(true);
                    return product;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getTotalCountOfProduct()  {
        int count = 0;
        String s = "SELECT * FROM Products where is_active=1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(s);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}
