package repository;

import javafx.collections.ObservableList;
import model.Product;

public interface ProductDao {

    ObservableList getProductList(int pageIndex, int rowsPerPage);

    ObservableList getProductListForComboBox();

    void addProduct(Product product);

    void deleteProductById(int id);

    Product isProductExist(String name);

    void updateProductIncreaseQuantity(Product newProduct, int id);

    void updateProduct(Product product, int oldProductId);

    Product getProductById(int id);

    int getTotalCountOfProduct();
}
