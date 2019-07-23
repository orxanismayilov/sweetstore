package service.serviceImpl;

import enums.UserRole;
import javafx.collections.ObservableList;
import model.Product;
import model.User;
import model.UserSession;
import org.apache.log4j.Logger;
import repository.ProductDao;
import service.ProductService;
import utils.LoadPropertyUtil;
import utils.NumberUtils;

import java.util.*;

public class ProductServiceImpl implements ProductService {

    private ProductDao productDao;
    private static Map<String, Map<Boolean, List<String>>> validation;
    private Properties errorProperties;
    private static Logger logger =Logger.getLogger(ProductServiceImpl.class);
    private UserSession userSession;


    private static String ERROR_PROPERTIES="sample/resource/properties/errors.properties";

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
        errorProperties= LoadPropertyUtil.loadPropertiesFile(ERROR_PROPERTIES);
        userSession=UserSession.getInstance();
    }

    public ObservableList getProductList(int pageIndex, int rowsPerPage) {
       return productDao.getProductList(pageIndex,rowsPerPage);
    }

    public Map addProduct(Product product) {
        if (product != null) {
            validation = isProductValid(product);
            if (!validation.get("nameError").containsKey(true) && !validation.get("quantityError").containsKey(true) && !validation.get("priceError").containsKey(true)) {
                Product existedProduct = productDao.isProductExist(product.getName());
                if (existedProduct == null) {
                    productDao.addProduct(product);
                    logger.info("New product :"+product.toString()+" "+userSession.getUser().toString());
                    return validation;
                } else {
                    if (existedProduct.getQuantity() + product.getQuantity() > 1000) {
                        Map<Boolean, List<String>> quantityMap = ProductServiceImpl.validation.get("quantityError");
                        List ls = quantityMap.get(false);
                        ls.add(errorProperties.getProperty("maxQuantity") + "--Current quantity is " + existedProduct.getQuantity());
                        quantityMap.remove(false);
                        quantityMap.put(true, ls);
                        validation.put("quantityError", quantityMap);
                        logger.error("Update product failed: Maximum quantity error.");
                        return validation;
                    } else {
                        productDao.updateProductIncreaseQuantity(product, existedProduct.getId());
                        logger.info("Update product:"+product.toString()+" by "+userSession.getUser().toString());
                        return validation;
                    }
                }
            }
        }
        return validation;
    }

    public Map updateProduct(Product product, int oldProductId) {
        logger.info("Product updating start");
        if(product!=null) {
            validation=isProductValid(product);
            if (!validation.get("nameError").containsKey(true) && !validation.get("quantityError").containsKey(true) && !validation.get("priceError").containsKey(true)) {
               productDao.updateProduct(product,oldProductId);
               logger.info("Product update:"+product.toString()+userSession.getUser().toString());
            }
        }else {
            logger.error("Couldnt update product , product is null. OldProductId:"+oldProductId);
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
                    logger.error(errorProperties.getProperty("nameSize"));
                } else {
                    product.setName(renameProduct(product.getName()));
                }
            } else {
                nameErrorList.add(errorProperties.getProperty("nullName"));
                logger.error(errorProperties.getProperty("nullName"));
                nameMap.put(true, nameErrorList);
            }
            List quantityErrorList = quantityMap.get(false);
            if (product.getQuantity() < 0) {
                quantityErrorList.add(errorProperties.getProperty("negativeQuantity"));
                logger.error(errorProperties.getProperty("negativeQuantity"));
            }
            if (product.getQuantity() > 1000) {
                quantityErrorList.add(errorProperties.getProperty("maxQuantity"));
                logger.error(errorProperties.getProperty("maxQuantity"));
            }
            if (!NumberUtils.isNumberInteger(String.valueOf(product.getQuantity()))) {
                quantityErrorList.add(errorProperties.getProperty("invalidNumber"));
                logger.error(errorProperties.getProperty("invalidNumber"+userSession.getUser().toString()));
            }
            if (!quantityErrorList.isEmpty()) {
                quantityMap.remove(false);
                quantityMap.put(true, quantityErrorList);
            }

            List priceErrorList = priceMap.get(false);
            if (product.getPrice() < 0) {
                priceErrorList.add(errorProperties.getProperty("negativePrice"));
                logger.error(errorProperties.getProperty("negativePrice"));
            }
            if (product.getPrice() > 1000) {
                priceErrorList.add(errorProperties.getProperty("maxPrice"));
                logger.error(errorProperties.getProperty("maxPrice"));
            }
            if (!NumberUtils.isNumberFloat(String.valueOf(product.getPrice()))) {
                priceErrorList.add(errorProperties.getProperty("invalidNumber"));
                logger.error(errorProperties.getProperty("invalidNumber"));
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

    public boolean deleteProductByID(int id) {
        User user=userSession.getUser();
        if (user.getRole().equals(UserRole.ADMIN)) {
            productDao.deleteProductById(id);
            logger.info("Product Id :"+id+" user :"+userSession.getUser().toString());
            return true;
        }
        return false;
    }

    public Product getProductById(int id) {
        Product product = productDao.getProductById(id);
        return product;
    }

    public ObservableList getProductListForComboBox() {
        return productDao.getProductListForComboBox();
    }

    public int getTotalCountOfProduct()  {
        return productDao.getTotalCountOfProduct();
    }

}
