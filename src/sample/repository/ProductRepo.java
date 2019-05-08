package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.dao.ProductDao;
import sample.model.Product;
import sample.utils.CopyListUtil;
import sample.utils.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Collections;

public class ProductRepo implements ProductDao {
    private ObservableList<Product> productList;
    private Connection connection;

    public ProductRepo() throws SQLException {
       connection= DBConnection.getConnection();
        Statement statement = connection.createStatement();
        String s = "SELECT * FROM Products";
        productList=FXCollections.observableArrayList();

        ResultSet rs = statement.executeQuery(s);
        while (rs.next()) {
            Product product=new Product();
            product.setId(rs.getInt("Id"));
            product.setName(rs.getString("Name"));
            product.setQuantity(rs.getInt("Quantity"));
            product.setPrice(rs.getFloat("Price"));
            product.setUpdateDate(LocalDateTime.now());
            if (rs.getInt("Is_Active")==1){
                product.setActive(true);
            } else {
                product.setActive(false);
            }
            productList.add(product);
        }
    }

    public ObservableList getProductList(){
        return copyList(productList,product -> product.isActive());
    }

    public void addProduct(Product product){
        product.setId(getProductNewId());
        productList.add(0,product);
    }

    public void deleteProductbyId(int id){
        for(Product product:productList){
            if (product.getId()==id){
                product.setActive(false);
            }
        }
    }

    public Product isProductExist(String name){
        for (Product p:productList){
            if (p.getName().equals(name)) {
                if (p.isActive()) {
                    return p;
                }
            }
        }
        return null;
    }

    public void updateProductIncreaseQuantity(Product newProduct, int id){
        for (Product p:productList){
            if (p.getId()==id){
                p.setQuantity(p.getQuantity()+newProduct.getQuantity());
                p.setPrice(newProduct.getPrice());
                p.setUpdateDate(LocalDateTime.now());
            }
        }
    }

    public void updateProduct(Product product, int oldProductId){
        for (Product p:productList){
            if (p.getId()==oldProductId){
                p.setName(product.getName());
                p.setPrice(product.getPrice());
            }
        }
    }

    public int getProductNewId() {
        return 0;

    }

    public Product getProductByName(String name){
        for(Product p:productList){
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public ObservableList getProductNames(){
        ObservableList<String> productNames= FXCollections.observableArrayList();
        for (Product product:productList){
            if (product.isActive()) {
                productNames.add(product.getName());
            }
        }
        Collections.sort(productNames);
        return productNames;
    }

    private ObservableList copyList(ObservableList<Product> list, CopyListUtil<Product> rule){
        ObservableList<Product> copiedList=FXCollections.observableArrayList();
        for(Product product:list){
            if(rule.check(product)) {
                copiedList.add(product);
            }
        }
        return copiedList;
    }

    public Product getProductById(int id) {
        for (Product product : productList) {
            if (product.getId() == id ) {
                return product;
            }
        }
        return null;
    }

    public int getTotalCountOfPrduct() {
        int count=0;
        for (Product p:productList) {
            count++;
        }
        return count;
    }
}