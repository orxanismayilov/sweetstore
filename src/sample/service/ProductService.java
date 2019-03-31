package sample.service;

import javafx.collections.ObservableList;
import sample.model.Product;
import sample.repository.ProductDummyRepo;
import sample.utils.Notification;

public class ProductService {

    private ProductDummyRepo productDummyRepo;
    ObservableList<Product> list;
    private int index;

    public ProductService() {
        productDummyRepo=new ProductDummyRepo();
    }

    public ObservableList getData(){
        return productDummyRepo.getProductList();
    }

    public Notification addData(Product product){
        Notification error=isProductValid(product);
        if (!error.hasError()) {
            Product existedProduct=productDummyRepo.isProductExist(product.getName());
            if (existedProduct == null) {
                int newId=productDummyRepo.getProductNewId();
                product.setId(newId);
                productDummyRepo.addProduct(product);
                return error;
            } else {
                product.setId(existedProduct.getId());
                productDummyRepo.updateProduct(product,existedProduct.getId());
                return error;
            }
        }
        return error;
    }

    private String reFixProduct(Product product){
            String finalName = product.getName().trim();
            finalName = finalName.substring(0, 1).toUpperCase() + finalName.substring(1);
            return finalName;
    }

    public Notification isProductValid(Product product){

        Notification errors=new Notification();
        if(product.getName().length()<3) {
            errors.addError("Name can't be empty or less 3 character.");
        } else product.setName(reFixProduct(product));
        if(product.getQuantity() <0 || product.getQuantity()>1000) errors.addError("Quantity can't be negative and greater then 1000.");
        int price=product.getPrice().intValue();
        if(price<0 || price>1000) errors.addError("Price can't be negative and greater then 1000.");
        return errors;
    }

    public void deleteProductbyID(int id){
        productDummyRepo.deleteProductbyId(id);
    }

    public  boolean updateProductNameandPrice(Product product){
        Notification errors=isProductValid(product);
        if (!errors.hasError()) {
            productDummyRepo.updateProductNameandPrice(product);
            return true;
        }
        return false;
    }

    public Product getProductByName(String name){
        return productDummyRepo.getProductByName(name);
    }

}
