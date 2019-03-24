package sample.service;

import javafx.collections.ObservableList;
import sample.model.Product;
import sample.repository.ProductDummyRepo;

import java.util.function.Predicate;

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
        //product.setName(reFixProduct(product));
      if (isProductValid(product)){
          //getProductIdByProductName(name);
          index=isProductExist(product.getName());
          if(index<0){
              product.setId(getProductNewId());
              productDummyRepo.addProduct(product);
              return true;
          }else {
              updateProduct(product,index);
              return true;
          }
      }else return false;
    }


    private String reFixProduct(Product product){
        String finalName=product.getName().trim();
        return finalName;
    }
    private boolean isProductValid(Product product){
        if(product.getQuantity() <0 ) return false;
        if(product.getName() ==null) return false;
        if(product.getPrice()<0) return false;
        return true;
    }

    public void deleteProductbyID(int id){
        //todo: migrate predicate to repo
        Predicate<Product> productPredicate=product -> product.getId()==id;
        productDummyRepo.deleteProduct(productPredicate);
    }

    private int getProductNewId(){
        //productDummyRepo.getProductNewId();
        index=productDummyRepo.getProductList().size()-1;
        Product product= (Product) productDummyRepo.getProductList().get(index);
        return product.getId()+1;
    }

    private boolean numberValidation(Product product){
        if (product.getPrice()<0 || product.getQuantity()<0)
            return false;
        else
            return true;
    }

    private int isProductExist(String name){
        //productDummyRepo.isProductExist();
        list=productDummyRepo.getProductList();
        index=0;
        for (Product p:list){
            if (p.getName().equals(name)) return index;
            index++;
        }
        return -1;
    }

    private void updateProduct(Product newProduct,int index){
        //TODO: productDummyRepo.updateProduct(index,newProduct);
        Product product= (Product) productDummyRepo.getProductList().get(index);
        product.setPrice(newProduct.getPrice());
        product.setQuantity(product.getQuantity()+newProduct.getQuantity());
    }
}
