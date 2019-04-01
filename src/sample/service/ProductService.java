package sample.service;

import javafx.collections.ObservableList;
import sample.model.Product;
import sample.repository.ProductDummyRepo;
import sample.utils.Notification;

public class ProductService {

    private ProductDummyRepo productDummyRepo;
    private final static String NAME_ERROR="Please enter valid name.";
    private final static String PRICE_ERROR="Price must be positive and less then 1000.";
    private final static String QUANTITY_ERROR="Quantity must be positive and less then 1000.";
    private final static String PRICE_REGEX="^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$";
    private final static String QUANTITY_REGEX="\\d*";
    private final static String NAME_REGEX="(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$";

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
        if(product.getName().length()<3 || !product.getName().matches(NAME_REGEX)) {
            errors.addError(NAME_ERROR);
        } else product.setName(reFixProduct(product));
        if(product.getQuantity() <0 || product.getQuantity()>1000 || !String.valueOf(product.getQuantity()).matches(QUANTITY_REGEX)) errors.addError(QUANTITY_ERROR);
        int price=product.getPrice().intValue();
        if(price<0 || price>1000 || !String.valueOf(price).matches(PRICE_REGEX)) errors.addError(PRICE_ERROR);
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

    public ObservableList getProductNames(){
        return productDummyRepo.getProductNames();
    }

}
