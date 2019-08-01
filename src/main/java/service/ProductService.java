package service;

import dtos.ProductsDTO;
import javafx.collections.ObservableList;
import model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    ProductsDTO getProductList(int pageIndex, int rowsPerPage);

    Map addProduct(Product product);

    Map updateProduct(Product product, int oldProductId);

    Map<String, Map<Boolean, List<String>>> isProductValid(Product product);

    boolean deleteProductByID(int id);

    Product getProductById(int id);

    ObservableList getProductListForComboBox();

    int getTotalCountOfProduct();
}
