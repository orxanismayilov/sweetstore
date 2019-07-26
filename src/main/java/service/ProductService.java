package service;

import javafx.collections.ObservableList;
import model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    ObservableList getProductList(int pageIndex, int rowsPerPage);

    Map addProduct(Product product);

    Map updateProduct(Product product, String oldProductId);

    Map<String, Map<Boolean, List<String>>> isProductValid(Product product);

    boolean deleteProductByID(String id);

    Product getProductById(String id);

    ObservableList getProductListForComboBox();

    int getTotalCountOfProduct();
}
