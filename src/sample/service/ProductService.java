package sample.service;

import javafx.collections.ObservableList;
import sample.repository.ProductDao;
import sample.model.Product;
import sample.repository.impl.ProductDaoImpl;
import sample.utils.LoadPropertyUtil;
import sample.utils.NumberUtils;

import java.sql.SQLException;
import java.util.*;

public class ProductService {

    private ProductDao productDao;
    private static Map<String, Map<Boolean, List<String>>> validation;
    private Properties errorProperties;

    private static String ERROR_PROPERTIES="C:\\Users\\Orxan\\Desktop\\Home Project\\Home Project\\src\\sample\\resource\\properties\\errors.properties";

    public ProductService(ProductDao productDao) throws SQLException {
        this.productDao = productDao;
        errorProperties= LoadPropertyUtil.loadPropertiesFile(ERROR_PROPERTIES);
    }

    public ObservableList getProductList(int pageIndex) {
       return productDao.getProductList(pageIndex);
    }

    public Map addProduct(Product product) {
        if (product != null) {
            validation = isProductValid(product);
            if (!validation.get("nameError").containsKey(true) && !validation.get("quantityError").containsKey(true) && !validation.get("priceError").containsKey(true)) {
                Product existedProduct = productDao.isProductExist(product.getName());
                if (existedProduct == null) {
                    productDao.addProduct(product);
                    return validation;
                } else {
                    if (existedProduct.getQuantity() + product.getQuantity() > 1000) {
                        Map<Boolean, List<String>> quantityMap = ProductService.validation.get("quantityError");
                        List ls = quantityMap.get(false);
                        ls.add(errorProperties.getProperty("maxQuantity") + "--Current quantity is " + existedProduct.getQuantity());
                        quantityMap.remove(false);
                        quantityMap.put(true, ls);
                        validation.put("quantityError", quantityMap);
                        return validation;
                    } else {
                        productDao.updateProductIncreaseQuantity(product, existedProduct.getId());
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
            if (!validation.get("nameError").containsKey(true) && !validation.get("quantityError").containsKey(true) && !validation.get("priceError").containsKey(true)) {
               productDao.updateProduct(product,oldProductId);
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
                    nameErrorList.add(errorProperties.getProperty("nameSize"));
                    nameMap.put(true, nameErrorList);
                } else {
                    product.setName(renameProduct(product.getName()));
                }
            } else {
                nameErrorList.add(errorProperties.getProperty("nullName"));
                nameMap.put(true, nameErrorList);
            }
            List quantityErrorList = quantityMap.get(false);
            if (product.getQuantity() < 0) {
                quantityErrorList.add(errorProperties.getProperty("negativeQuantity"));
            }
            if (product.getQuantity() > 1000) {
                quantityErrorList.add(errorProperties.getProperty("maxQuantity"));
            }
            if (!NumberUtils.isNumberInteger(String.valueOf(product.getQuantity()))) {
                quantityErrorList.add(errorProperties.getProperty("invalidNumber"));
            }
            if (!quantityErrorList.isEmpty()) {
                quantityMap.remove(false);
                quantityMap.put(true, quantityErrorList);
            }

            List priceErrorList = priceMap.get(false);
            if (product.getPrice() < 0) {
                priceErrorList.add(errorProperties.getProperty("negativePrice"));
            }
            if (product.getPrice() > 1000) {
                priceErrorList.add(errorProperties.getProperty("maxPrice"));
            }
            if (!NumberUtils.isNumberFloat(String.valueOf(product.getPrice()))) {
                priceErrorList.add(errorProperties.getProperty("invalidNumber"));
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
        productDao.deleteProductbyId(id);
    }

    public Product getProductByName(String name) {
        return productDao.getProductByName(name);
    }

    public ObservableList getProductNames() {
        return productDao.getProductNames();
    }

    public Product getProductById(int id) throws Exception {
        Product product = productDao.getProductById(id);
        return product;
    }

    public ObservableList getProductListForComboBox() {
        return productDao.getProductListForComboBox();
    }

    public int getTotalCountOfProduct()  {
        try {
            return productDao.getTotalCountOfPrduct();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
