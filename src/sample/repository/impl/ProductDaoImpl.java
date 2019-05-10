package sample.repository.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Product;
import sample.repository.ProductDao;
import sample.utils.CopyListUtil;
import sample.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collections;

public class ProductDaoImpl implements ProductDao {

    @Override
    public ObservableList getProductList(int pageIndex,int rowsPerPage) {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Products where is_active=1 Limit ?,? ";
        int totalCount=getTotalCountOfPrduct();
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
                   product.setUpdateDate(LocalDateTime.now());
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
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql="select * from Products where Is_Active=1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("Id"));
                product.setName(rs.getString("Name"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setPrice(rs.getFloat("Price"));
                product.setUpdateDate(LocalDateTime.now());
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
        String s = "INSERT INTO products (Name,Quantity,Price,Update_Date,Is_Active) VALUES (?,?,?,?,?)";
        try(Connection connection=DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(s))
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
    public void deleteProductbyId(int id) {
        String s = "UPDATE products set Is_Active =0 where Id = ? ";
        try(Connection connection=DBConnection.getConnection();
            PreparedStatement ps=connection.prepareStatement(s))
        {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product isProductExist(String name) {
        String s = "SELECT * from products  where Name = ? ";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps1 = connection.prepareStatement(s);
             PreparedStatement ps2 = connection.prepareStatement(s);
        ) {
            ps1.setString(1, name);
            ps2.setString(1, name);
            try (ResultSet rs1 = ps1.executeQuery();
                 ResultSet rs2 = ps2.executeQuery()) {
                Product product = new Product();
                if (rs1.next()) {
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
                }
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateProductIncreaseQuantity(Product newProduct, int id) {
        String s = "UPDATE products set Quantity =Quantity+? , Price = ? where Id = ? ";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps=connection.prepareStatement(s))
        {
            ps.setInt(1, newProduct.getQuantity());
            ps.setFloat(2, newProduct.getPrice());
            ps.setInt(3,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product, int oldProductId) {
       return;
    }

    public Product getProductByName(String name) {
        return null;
    }

    public ObservableList getProductNames() {
        return null;
    }

    public Product getProductById(int id) {
        return null;
    }

    public int getTotalCountOfPrduct()  {
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
