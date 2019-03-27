package sample.service;

import javafx.collections.ObservableList;
import sample.model.Product;
import sample.repository.ProductDummyRepo;

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

    public boolean addData(Product product){
        product.setName(reFixProduct(product));
        if (isProductValid(product)) {
            Product existedProduct=productDummyRepo.isProductExist(product.getName());
            if (existedProduct == null) {
                int newId=productDummyRepo.getProductNewId();
                product.setId(newId);
                productDummyRepo.addProduct(product);
                return true;
            } else {
                product.setId(existedProduct.getId());
                productDummyRepo.updateProduct(product,existedProduct.getId());
                return true;
            }
        }
        return false;
    }

    private String reFixProduct(Product product){
        String finalName=product.getName().trim();
        finalName=finalName.substring(0, 1).toUpperCase() + finalName.substring(1);
        return finalName;
    }

    public boolean isProductValid(Product product){
        if(product.getQuantity() <0 ||product.getQuantity()>1000 ) return false;
        if(product.getName() ==null) return false;
        if(product.getPrice()<0 || product.getPrice()>1000) return false;
        return true;
    }

    public void deleteProductbyID(int id){
        productDummyRepo.deleteProductbyId(id);
    }

}
