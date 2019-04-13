package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Product;
import sample.utils.CopyListUtil;

import java.time.LocalDateTime;
import java.util.Collections;

public class ProductDummyRepo  {

    private static int idCounter=4;
    private static ObservableList<Product>  productList=FXCollections.observableArrayList(
            new Product(1,"Paxlava",50, (float) 1.2,LocalDateTime.now()),
            new Product(2,"Wekerbura",23,2,LocalDateTime.now()),
            new Product(3,"Tort",12, (float) 2.30,LocalDateTime.now())
    );

    public ObservableList getProductList(){
        return copyList(productList,product -> product.isActive());
    }

    public void addProduct(Product product){
        product.setId(getProductNewId());
        productList.add(product);
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
        return idCounter++;

    }

    private int findMaxId(){
        //TODO: find max id
        int maxId=-1;
        for(Product product:productList){
            if(maxId<product.getId()){
               maxId=product.getId();
            }
        }
        return maxId;
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


    private ObservableList copyList(ObservableList<Product> list,CopyListUtil<Product> rule){
         ObservableList<Product> copiedList=FXCollections.observableArrayList();
         for(Product product:list){
             if(rule.check(product)) {
                 copiedList.add(product);
             }
         }
         return copiedList;
    }

    public Product getProductById(int id) throws Exception {
        for (Product product : productList) {
            if (product.getId() == id ) {
                return product;
            }
        }
        return null;
    }
}
