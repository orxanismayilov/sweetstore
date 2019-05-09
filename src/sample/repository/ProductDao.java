package sample.repository;

import javafx.collections.ObservableList;
import sample.model.Product;

import java.sql.SQLException;

public interface ProductDao {
    ObservableList getProductList() throws SQLException;
    ObservableList getProductList(int page,int count) throws SQLException;

    void addProduct(Product product);

    void deleteProductbyId(int id);

    Product isProductExist(String name);

    void updateProductIncreaseQuantity(Product newProduct, int id);


    void updateProduct(Product product, int oldProductId);

    Product getProductByName(String name);

    ObservableList getProductNames();

    Product getProductById(int id);

    int getTotalCountOfPrduct() throws SQLException;
}
