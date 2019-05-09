package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Product;
import sample.utils.CopyListUtil;
import sample.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collections;

public class ProductRepo implements ProductDao {
    private ObservableList<Product> productList;
    private Connection connection;

    public ProductRepo() {
        productList = FXCollections.observableArrayList();
    }

    @Override
    public ObservableList getProductList() {
        ObservableList<Product> productList;
        connection = DBConnection.getConnection();
        productList = FXCollections.observableArrayList();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String s = "SELECT * FROM Products";

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(s);
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("Id"));
                product.setName(rs.getString("Name"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setPrice(rs.getFloat("Price"));
                product.setUpdateDate(LocalDateTime.now());
                if (rs.getInt("Is_Active") == 1) {
                    product.setActive(true);
                } else {
                    product.setActive(false);
                }
                productList.add(product);
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
        return copyList(productList, Product::isActive);
    }

    public ObservableList getProductList(int page,int count) {
        //page =20
        //count=10
        ObservableList<Product> productList;
        connection = DBConnection.getConnection();
        productList = FXCollections.observableArrayList();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String s = "SELECT * FROM Products where is_active=1 ";// from 200-210 limit 10 offset 200

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(s);
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("Id"));
                product.setName(rs.getString("Name"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setPrice(rs.getFloat("Price"));
                product.setUpdateDate(LocalDateTime.now());
                if (rs.getInt("Is_Active") == 1) {
                    product.setActive(true);
                } else {
                    product.setActive(false);
                }
                productList.add(product);
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
        return copyList(productList, product -> product.isActive());
    }

    private PreparedStatement createPreparedStatement(Connection con, int userId) throws SQLException {
        String sql = "SELECT id, username FROM users WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);
        return ps;
    }
    //@Override
    public ObservableList getProductList1() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String s = "SELECT * FROM Products where is_active=1";

        //ResultSet rs = null;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(s);
             ResultSet rs = ps.executeQuery()) {
           // rs = statement.executeQuery(s);
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("Id"));
                product.setName(rs.getString("Name"));
                product.setQuantity(rs.getInt("Quantity"));
                product.setPrice(rs.getFloat("Price"));
                product.setUpdateDate(LocalDateTime.now());
                /*if (rs.getInt("Is_Active") == 1) {
                    product.setActive(true);
                } else {
                    product.setActive(false);
                }*/
                product.setActive(rs.getInt("Is_Active") == 1);

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




       /* product.setId(getProductNewId());
        productList.add(0,product);*/
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

    public int getProductNewId() {
        return 0;

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

    public int getTotalCountOfPrduct() throws SQLException {
        int count = 0;
        connection = DBConnection.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String s = "SELECT * FROM Products";

        ResultSet rs = null;
        try {
            rs = statement.executeQuery(s);
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return count;
    }
}
