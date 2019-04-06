package sample.service;

import javafx.collections.ObservableList;
import sample.model.Product;
import sample.repository.ProductDummyRepo;
import sample.utils.Notification;

import java.lang.ref.PhantomReference;
import java.util.HashMap;
import java.util.Map;

public class ProductService {

    private ProductDummyRepo productDummyRepo;
    private Notification errors;
    private final static String NUll_NAME_ERROR="Name can't be empty.";
    private final static String NEGATIVE_PRICE_ERROR="Price must be positive.";
    private final static String MAX_PRICE_ERROR="Price must be less than 1000.";
    private final static String VALID_PRICE_ERROR="Please enter valid price.";
    private final static String NEGATIVE_QUANTITY_ERROR="Quantity must be positive.";
    private final static String MAX_QUANTITY_ERROR="Quantity must be less than 1000.";
    private final static String VAlID_QUANTITY_ERROR="Quantity must be valid.";
    private final static String PRICE_REGEX="^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$";
    private final static String QUANTITY_REGEX="\\d*";
    private final static String NAME_REGEX="(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$";
    private static Map<String,Boolean> validation;
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
                if (existedProduct.getQuantity()+product.getQuantity()>1000){
                    error=new Notification();
                    error.addError(MAX_QUANTITY_ERROR);
                    validation.put("quantityError",true);
                    return error;
                }else {
                    product.setId(existedProduct.getId());
                    productDummyRepo.updateProduct(product, existedProduct.getId());
                    return error;
                }
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
        validation=new HashMap<>();
        validation.put("nameError",false);
        validation.put("priceError",false);
        validation.put("quantityError",false);
        errors= new Notification();
        if(product.getName().length()<3 || !product.getName().matches(NAME_REGEX)) {
            errors.addError(NUll_NAME_ERROR);
        } else product.setName(reFixProduct(product));
        if(product.getQuantity() <0 ) {
            errors.addError(NEGATIVE_QUANTITY_ERROR); validation.put("quantityError",true);
        }
        if (product.getQuantity()>1000) {
            errors.addError(MAX_QUANTITY_ERROR); validation.put("quantityError",true);
        }
        if (!String.valueOf(product.getQuantity()).matches(QUANTITY_REGEX)) {
            errors.addError(VAlID_QUANTITY_ERROR); validation.put("quantityError",true);
        }
        int price=product.getPrice().intValue();
        if(price<0) {
            errors.addError(NEGATIVE_PRICE_ERROR); validation.put("priceError",true);
        }
        if (price>1000) {
            errors.addError(MAX_PRICE_ERROR); validation.put("priceError",true);
        }
        if(!String.valueOf(price).matches(PRICE_REGEX)) {
            errors.addError(VALID_PRICE_ERROR); validation.put("priceError",true);
        }
        return errors;
    }

    public void deleteProductbyID(int id){
        productDummyRepo.deleteProductbyId(id);
    }

    public Product getProductByName(String name){
        return productDummyRepo.getProductByName(name);
    }

    public ObservableList getProductNames(){
        return productDummyRepo.getProductNames();
    }

    public Product getProductById(int id) throws Exception {
        Product product=productDummyRepo.getProductById(id);
        return product;
    }

    public static Map<String, Boolean> getValidation() {
        return validation;
    }
}
