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
            new Product(3,"Tort",12,2.30,LocalDate.of(2019,03,18))
    );

    public ObservableList getProductList(){
        return copyList(productList);
    }

    public void addProduct(Product product){
        productList.add(product);
    }

    public void deleteProduct(Predicate<Product> product){
        productList.removeIf(product);
    }

    public void deleteProductbyId(int id){
        Predicate<Product> productPredicate=product -> product.getId()==id;
        deleteProduct(productPredicate);
    }

    public Product isProductExist(String name){
        for (Product p:productList){
            if (p.getName().equals(name)) return p;
        }
        return null;
    }

    public void updateProduct(Product newProduct,int id){
        for (Product p:productList){
            if (p.getId()==id){
                p.setQuantity(p.getQuantity()+newProduct.getQuantity());
                p.setPrice(newProduct.getPrice());
                p.setUpdateDate(LocalDate.now());
            }
        }
    }

    public int getProductNewId() {
        int index=getProductList().size()-1;
        Product product= (Product) getProductList().get(index);
        return product.getId()+1;
    }

    private ObservableList copyList(ObservableList<Product> list){
         ObservableList<Product> copiedList=FXCollections.observableArrayList();
         for(Product product:list){
             copiedList.add(product);
         }

         return copiedList;
    }
}
