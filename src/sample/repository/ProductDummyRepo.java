package sample.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

public class ProductDummyRepo {

    private static int idCounter=4;
    private static ObservableList<Product>  productList=FXCollections.observableArrayList(
            new Product(1,"Paxlava",50,BigDecimal.valueOf(1.50),LocalDate.of(2018,12,03)),
            new Product(2,"Wekerbura",23,BigDecimal.valueOf(2),LocalDate.of(2018,01,15)),
            new Product(3,"Tort",12,BigDecimal.valueOf(2.30),LocalDate.of(2019,03,18))
    );

    public ObservableList getProductList(){
        return copyList(productList,product -> product.isActive());
    }

    public void addProduct(Product product){
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

    public void updateProduct(Product newProduct,int id){
        for (Product p:productList){
            if (p.getId()==id){
                p.setQuantity(p.getQuantity()+newProduct.getQuantity());
                p.setPrice(newProduct.getPrice());
                p.setUpdateDate(LocalDate.now());
            }
        }
    }

    public void updateProductNameandPrice(Product product){
        for (Product p:productList){
            if (p.getId()==product.getId()){
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


    private ObservableList copyList(ObservableList<Product> list,CheckProductIsActive rule){
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
    @FunctionalInterface
    private interface CheckProductIsActive {
        public boolean check(Product product);
    }
}
