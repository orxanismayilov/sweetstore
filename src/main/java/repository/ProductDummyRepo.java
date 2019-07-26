package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;
import utils.CopyListUtil;

import java.time.LocalDateTime;
import java.util.Collections;

public class ProductDummyRepo  {

    private static int idCounter=4;
    private static ObservableList<Product>  productList=FXCollections.observableArrayList();

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
                p.setUpdateDate(LocalDateTime.now().toString());
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

    public String getProductNewId() {
        return idCounter++;

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
        ObservableList<String> productNames=FXCollections.observableArrayList();
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
