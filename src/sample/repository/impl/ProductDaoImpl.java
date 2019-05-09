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
    private ObservableList<Product> productList;
    private Connection connection;
    private final int rowsPerPage=10;

    public ProductDaoImpl() {
        productList = FXCollections.observableArrayList();
    }

    private PreparedStatement createPreparedStatement(Connection con, int fromIndex, int toIndex) throws SQLException {
        String sql = "SELECT * FROM Products where is_active=1 Limit ?,? ";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, fromIndex);
        ps.setInt(2,toIndex);
        return ps;
    }
    @Override
    public ObservableList getProductList(int pageIndex) {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        int totalCount=getTotalCountOfPrduct();
        int fromIndex=pageIndex*rowsPerPage;
        int toIndex=Math.min(fromIndex+rowsPerPage,totalCount);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = createPreparedStatement(con,fromIndex,toIndex);
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
        connection = DBConnection.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(s);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getQuantity());
            preparedStatement.setFloat(3, product.getPrice());
            preparedStatement.setDate(4, Date.valueOf("2015-03-31"));
            preparedStatement.setInt(5, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteProductbyId(int id) {
        String s = "UPDATE products set Is_Active =? where Id = ? ";
        connection = DBConnection.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(s);
            preparedStatement.setInt(1, 0);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Product isProductExist(String name) {
        String s = "SELECT * from products  where Name = ? ";
        connection = DBConnection.getConnection();
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement(s);
            PreparedStatement preparedStatement2 = connection.prepareStatement(s);
            preparedStatement1.setString(1, name);
            preparedStatement2.setString(1,name);
            rs1 = preparedStatement1.executeQuery();
            rs2 = preparedStatement2.executeQuery();
                Product product = new Product();
                if(rs1.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void updateProductIncreaseQuantity(Product newProduct, int id) {
        String s = "UPDATE products set Quantity =Quantity+? , Price = ? where Id = ? ";
        connection = DBConnection.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(s);
            preparedStatement.setInt(1, newProduct.getQuantity());
            preparedStatement.setFloat(2, newProduct.getPrice());
            preparedStatement.setInt(3,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateProduct(Product product, int oldProductId) {
        for (Product p : productList) {
            if (p.getId() == oldProductId) {
                p.setName(product.getName());
                p.setPrice(product.getPrice());
            }
        }
    }

    public Product getProductByName(String name) {
        for (Product p : productList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public ObservableList getProductNames() {
        ObservableList<String> productNames = FXCollections.observableArrayList();
        for (Product product : productList) {
            if (product.isActive()) {
                productNames.add(product.getName());
            }
        }
        Collections.sort(productNames);
        return productNames;
    }

    private ObservableList copyList(ObservableList<Product> list, CopyListUtil<Product> rule) {
        ObservableList<Product> copiedList = FXCollections.observableArrayList();
        for (Product product : list) {
            if (rule.check(product)) {
                copiedList.add(product);
            }
        }
        return copiedList;
    }

    public Product getProductById(int id) {
        for (Product product : productList) {
            if (product.getId() == id) {
                return product;
            }
        }
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
