package sample.service;

import javafx.collections.ObservableList;
import sample.model.Product;
import sample.repository.ProductDummyRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {

    private ProductDummyRepo productDummyRepo;
    private final static String NUll_NAME_ERROR = "Name can't be empty.";
    private final static String NAME_SIZE_ERROR = "Name must contain 3 characters.";
    private final static String NEGATIVE_PRICE_ERROR = "Price must be positive.";
    private final static String MAX_PRICE_ERROR = "Price must be less than 1000.";
    private final static String INVALID_NUMBER_ERROR = "Please enter valid number.";
    private final static String NEGATIVE_QUANTITY_ERROR = "Quantity must be positive.";
    private final static String MAX_QUANTITY_ERROR = "Quantity must be less than 1000.";
    private final static String PRICE_REGEX = "^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$";
    private final static String QUANTITY_REGEX = "\\d*";
    private static Map<String, Map<Boolean, List<String>>> validation;

    public ProductService() {
        productDummyRepo = new ProductDummyRepo();
    }

    public ObservableList getProductList() {
        return productDummyRepo.getProductList();
    }

    public Map addProduct(Product product) {
        if (product != null) {
            validation = isProductValid(product);
            if (!validation.get("nameError").containsKey(true) && !validation.get("nameError").containsKey(true) && !validation.get("nameError").containsKey(true)) {
                Product existedProduct = productDummyRepo.isProductExist(product.getName());
                if (existedProduct == null) {
                    productDummyRepo.addProduct(product);
                    return validation;
                } else {
                    if (existedProduct.getQuantity() + product.getQuantity() > 1000) {
                        Map<Boolean, List<String>> quantityMap = ProductService.validation.get("quantityError");
                        List ls = quantityMap.get(false);
                        ls.add(MAX_QUANTITY_ERROR + "--Current quantity is " + existedProduct.getQuantity());
                        quantityMap.remove(false);
                        quantityMap.put(true, ls);
                        validation.put("quantityError", quantityMap);
                        return validation;
                    } else {
                        productDummyRepo.updateProductIncreaseQuantity(product, existedProduct.getId());
                        return validation;
                    }
                }
            }
        }
        return validation;
    }

    public Map updateProduct(Product product, int oldProductId) {
        if(product!=null) {
            validation=isProductValid(product);
            if (!validation.get("nameError").containsKey(true) && !validation.get("nameError").containsKey(true) && !validation.get("nameError").containsKey(true)) {
               productDummyRepo.updateProduct(product,oldProductId);
            }
        }
        return validation;
    }

    private String renameProduct(String productName) {
        String finalName = "";
        if (productName != null) {
            finalName = productName.toLowerCase().trim();
        }
        finalName = finalName.substring(0, 1).toUpperCase() + finalName.substring(1);
        return finalName;
    }

    public Map<String, Map<Boolean, List<String>>> isProductValid(Product product) {
        validation = new HashMap<>();
        Map<Boolean, List<String>> nameMap = new HashMap<>();
        Map<Boolean, List<String>> priceMap = new HashMap<>();
        Map<Boolean, List<String>> quantityMap = new HashMap<>();
        nameMap.put(false, new ArrayList<>());
        priceMap.put(false, new ArrayList<>());
        quantityMap.put(false, new ArrayList<>());
        if (product != null) {
            List nameErrorList = nameMap.get(false);
            if (product.getName() != null) {
                String name = product.getName();
                if (name.trim().length() < 2) {
                    nameErrorList.add(NAME_SIZE_ERROR);
                    nameMap.put(true, nameErrorList);
                } else {
                    product.setName(renameProduct(product.getName()));
                }
            } else {
                nameErrorList.add(NUll_NAME_ERROR);
                nameMap.put(true, nameErrorList);
            }
            List quantityErrorList = quantityMap.get(false);
            if (product.getQuantity() < 0) {
                quantityErrorList.add(NEGATIVE_QUANTITY_ERROR);
            }
            if (product.getQuantity() > 1000) {
                quantityErrorList.add(MAX_QUANTITY_ERROR);
            }
            if (!String.valueOf(product.getQuantity()).matches(QUANTITY_REGEX)) {
                quantityErrorList.add(INVALID_NUMBER_ERROR);
            }
            if (!quantityErrorList.isEmpty()) {
                quantityMap.remove(false);
                quantityMap.put(true, quantityErrorList);
            }

            List priceErrorList = priceMap.get(false);
            if (product.getPrice() < 0) {
                priceErrorList.add(NEGATIVE_PRICE_ERROR);
            }
            if (product.getPrice() > 1000) {
                priceErrorList.add(MAX_PRICE_ERROR);
            }
            if (!String.valueOf(product.getPrice()).matches(PRICE_REGEX)) {
                priceErrorList.add(INVALID_NUMBER_ERROR);
            }
            if (!priceErrorList.isEmpty()) {
                priceMap.remove(false);
                priceMap.put(true, priceErrorList);
            }

            validation.put("nameError", nameMap);
            validation.put("quantityError", quantityMap);
            validation.put("priceError", priceMap);
            if (nameMap.containsKey(true) || quantityMap.containsKey(true) || priceMap.containsKey(true)) {
                return validation;
            }

        }
        return validation;
    }

    public void deleteProductbyID(int id) {
        productDummyRepo.deleteProductbyId(id);
    }

    public Product getProductByName(String name) {
        return productDummyRepo.getProductByName(name);
    }

    public ObservableList getProductNames() {
        return productDummyRepo.getProductNames();
    }

    public Product getProductById(int id) throws Exception {
        Product product = productDummyRepo.getProductById(id);
        return product;
    }

}
