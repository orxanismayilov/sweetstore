package service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;
import model.ResponseObject;
import model.User;
import model.UserSession;
import org.apache.log4j.Logger;
import repository.ProductDao;
import service.ProductService;
import utils.LoadPropertyUtil;
import utils.NumberUtils;
import utils.RestClientUtil;

import java.util.*;

public class ProductServiceImpl implements ProductService {
    private Properties errorProperties;
    private static Logger logger =Logger.getLogger(ProductServiceImpl.class);
    private UserSession userSession;
    private Properties uriProperties;

    private static String URI_PROPERTIES="/resources/properties/resource-uri.properties";
    private static String ERROR_PROPERTIES="/resources/properties/errors.properties";

    public ProductServiceImpl() {
        errorProperties= LoadPropertyUtil.loadPropertiesFile(ERROR_PROPERTIES);
        userSession=UserSession.getInstance();
        uriProperties=LoadPropertyUtil.loadPropertiesFile(URI_PROPERTIES);
    }

    public ObservableList<Product> getProductList(int pageIndex, int rowsPerPage) {
        // TO DO response objecte tezden baxaciyiq.
        String uri=uriProperties.getProperty("producturi")+"?startPage="+pageIndex+"&rowsPerPage="+rowsPerPage;
        ResponseObject responseObject= RestClientUtil.getResourceList(uri);
        ObjectMapper mapper=new ObjectMapper();
        List<Product> list = mapper.convertValue(responseObject.getData(), new TypeReference<List<Product>>(){});
        return FXCollections.observableArrayList(list);
    }

    public Map addProduct(Product product) {
        Map<String, Map<Boolean, List<String>>> validation=isProductValid(product);
        String uri=uriProperties.getProperty("producturi");
            if (!validation.get("nameError").containsKey(true) && !validation.get("quantityError").containsKey(true) && !validation.get("priceError").containsKey(true)) {
                RestClientUtil.addNewResource(product,uri);
                logger.info("New product :"+product.toString()+" "+userSession.getUser().toString());
            }
        return validation;
    }

    public Map updateProduct(Product product, String oldProductId) {
        logger.info("Product updating start");
        String uri=uriProperties.getProperty("producturi");
        Map<String, Map<Boolean, List<String>>> validation=new HashMap<>();
        if(product!=null) {
            validation=isProductValid(product);
            if (!validation.get("nameError").containsKey(true) && !validation.get("quantityError").containsKey(true) && !validation.get("priceError").containsKey(true)) {
                RestClientUtil.updateResource(uri,oldProductId,product);
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
        Map<String, Map<Boolean, List<String>>> validation = new HashMap<>();
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

    public boolean deleteProductByID(String id) {
        User user=userSession.getUser();
        String uri=uriProperties.getProperty("producturi");
        if (user.getRole().equals(UserRole.ADMIN)) {
            RestClientUtil.deleteResource(uri,id);
            logger.info("Product Id :"+id+" user :"+userSession.getUser().toString());
            return true;
        }
        return false;
    }

    public Product getProductById(String id) {
        String uri=uriProperties.getProperty("producturi");
        ResponseObject responseObject= RestClientUtil.getSingleResource(uri,id);
        return (Product) responseObject.getData();
    }

    public ObservableList getProductListForComboBox() {
        String uri=uriProperties.getProperty("producturi")+"/combo-box";
        ResponseObject responseObject=RestClientUtil.getResourceList(uri);
        ObjectMapper mapper=new ObjectMapper();
        List<Product> list=mapper.convertValue(responseObject.getData(),new TypeReference<List<Product>>(){});
        return FXCollections.observableArrayList(list);
    }

    public int getTotalCountOfProduct()  {
        String uri=uriProperties.getProperty("producturi");
        ResponseObject responseObject=RestClientUtil.getSingleResource(uri,"/count");
        return (int) responseObject.getData();
    }

}
