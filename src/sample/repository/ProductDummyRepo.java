package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Product;

import java.time.LocalDate;
import java.util.function.Predicate;

public class ProductDummyRepo {
    private static ObservableList<Product>  productList=FXCollections.observableArrayList(
            new Product(1,"Paxlava",50,1.50,LocalDate.of(2018,12,03)),
            new Product(2,"Wekerbura",23,2,LocalDate.of(2018,01,15)),
            new Product(3,"tort",12,2.30,LocalDate.of(2019,03,18))
    );



    public ObservableList getProductList(){
        //todo: return new arraylist
        return productList;
    }

    public void addProduct(Product product){
        productList.add(product);
    }

    public void deleteProduct(Predicate<Product> product){
        productList.removeIf(product);
    }
}
