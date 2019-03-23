package sample.service;

import javafx.collections.ObservableList;
import sample.model.Product;
import sample.repository.ProductDummyRepo;

import java.util.function.Predicate;

public class ProductService {
    private ProductDummyRepo productDummyRepo;

    public ProductService() {
        productDummyRepo=new ProductDummyRepo();
    }

    public ObservableList getData(){
        return productDummyRepo.getProductList();
    }

    public void addData(Product product){
        productDummyRepo.addProduct(product);
    }

    public void deleteProductbyID(int id){
        Predicate<Product> productPredicate=product -> product.getId()==id;
        productDummyRepo.deleteProduct(productPredicate);
    }

    private int getProductNewId(){
        // TODO: get last item id;++
        return 4;
    }
}
