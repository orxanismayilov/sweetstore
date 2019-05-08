package sample.dao;

import javafx.collections.ObservableList;
import sample.model.Product;

public interface ProductDao {
    ObservableList getProductList();

    void addProduct(Product product);

    void deleteProductbyId(int id);

    Product isProductExist(String name);

    void updateProductIncreaseQuantity(Product newProduct, int id);


    void updateProduct(Product product, int oldProductId);

    int getProductNewId() ;

    Product getProductByName(String name);

    ObservableList getProductNames();

    Product getProductById(int id);

    int getTotalCountOfPrduct();
}
