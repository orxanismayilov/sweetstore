package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Product;
import repository.impl.ProductDaoImpl;
import service.ProductService;
import service.serviceImpl.ProductServiceImpl;
import utils.AlertUtil;
import utils.LoadPropertyUtil;
import utils.ScreenUtils;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private ProductService productService;
    private Stage fxmlControllerStage;
    private Properties fxlmProperties;
    private Properties appProperties;
    private final int rowsPerPage=10;

    private final static String FXML_PROPERTIES_URL = "/resources/properties/fxmlurls.properties";
    private final static String APP_PROPERTIES_URL = "/resources/properties/application.properties";
    private final static String PRODUCTNOTEXIST_ALERT = "Product not exist";
    private final static String DELETE_ALERT_TEXT = "Are you sure ?";
    private final static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static String NUM_FORMAT_PATTERN = "#,###,###,##0.00";
    private static final Image imageDelete = new Image("/resources/images/trash_26px.png");
    private static final Image imageUpdate = new Image("/resources/images/edit_property_26px.png");
    private static final Image imageInfo = new Image("/resources/images/info_24px.png");
    private static String MANAT_SYMBOL = "\u20BC";
    private TableView<Product> tableProduct;
    private Pagination pages;

    @FXML
    private BorderPane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService = new ProductServiceImpl(new ProductDaoImpl());
        createTable();
        paginationSetup();
        fxlmProperties = LoadPropertyUtil.loadPropertiesFile(FXML_PROPERTIES_URL);
        appProperties = LoadPropertyUtil.loadPropertiesFile(APP_PROPERTIES_URL);
    }

    public void paginationSetup() {
        int numOfPages = 1;
        int listSize=productService.getTotalCountOfProduct();
        if (listSize % rowsPerPage == 0) {
            numOfPages = listSize / rowsPerPage;
        } else if (listSize > rowsPerPage) {
            numOfPages = listSize / rowsPerPage + 1;
        }
        pages=new Pagination(numOfPages,0);
        pages.setPageFactory(this::createPage);
        pane.centerProperty().setValue(pages);
    }

    private void createTable() {
        tableProduct=new TableView<>();
        TableColumn<Product,Integer> clmID=new TableColumn<>("Id");
        TableColumn<Product,Float> clmPrice=new TableColumn<>("Price");
        TableColumn<Product,LocalDateTime> clmLastUpdate=new TableColumn<>("Last Update");
        TableColumn<Product,String> clmName=new TableColumn<>("Name");
        TableColumn<Product,Integer> clmQuantity=new TableColumn<>("Quantity");
        TableColumn<Product,Void> clmAction=new TableColumn<>("Action");
        tableProduct.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableProduct.getColumns().addAll(clmID,clmName,clmPrice,clmQuantity,clmLastUpdate,clmAction);

        clmID.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmLastUpdate.setCellValueFactory(new PropertyValueFactory<>("updateDate"));
        clmQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        clmPrice.setCellFactory(tc -> new TableCell<Product, Float>() {
            @Override
            protected void updateItem(Float price, boolean empty) {
                super.updateItem(price, empty);
                DecimalFormat numberFormat = new DecimalFormat(NUM_FORMAT_PATTERN);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(numberFormat.format(price)) + MANAT_SYMBOL);
                }
            }
        });
        clmAction.setCellFactory(tc -> new TableCell<Product, Void>() {
            final ImageView buttonDeleteGraphic = new ImageView();
            final ImageView buttonUpdateGraphic = new ImageView();
            final ImageView buttonInfoGraphic = new ImageView();
            private final Button buttonDelete = new Button();
            private final Button buttonUpdate = new Button();
            private final Button buttonInfo = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                buttonDeleteGraphic.setImage(imageDelete);
                buttonDeleteGraphic.setFitWidth(12);
                buttonDeleteGraphic.setFitHeight(12);
                buttonUpdateGraphic.setImage(imageUpdate);
                buttonUpdateGraphic.setFitWidth(12);
                buttonUpdateGraphic.setFitHeight(12);
                buttonInfoGraphic.setImage(imageInfo);
                buttonInfoGraphic.setFitHeight(14);
                buttonInfoGraphic.setFitWidth(14);
                buttonUpdate.setGraphic(buttonUpdateGraphic);
                buttonDelete.setGraphic(buttonDeleteGraphic);
                buttonInfo.setGraphic(buttonInfoGraphic);
                HBox pane = new HBox(buttonDelete, buttonUpdate, buttonInfo);
                pane.setSpacing(10);
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);

                    buttonDelete.setOnAction((ActionEvent eventDelete) -> {
                        Product selectedProduct = (Product) getTableRow().getItem();
                        buttonDeleteAction(selectedProduct.getId());
                        paginationSetup();
                    });

                    buttonInfo.setOnAction((ActionEvent eventInfo) -> {
                        Product product = (Product) getTableRow().getItem();
                        buttonInfoAction(product.getId());
                        popUpWindowSetup(eventInfo, appProperties.getProperty("infoproducttitle"));
                    });

                    buttonUpdate.setOnAction((ActionEvent eventUpdate) -> {
                        Product product = (Product) getTableRow().getItem();
                        updateButtonAction(product);
                        popUpWindowSetup(eventUpdate, appProperties.getProperty("updateproducttitle"));
                        //loadData();
                    });
                }
            }
        });
        clmLastUpdate.setCellFactory(tc -> new TableCell<Product, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime time, boolean empty) {
                super.updateItem(time, empty);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
                if (empty) {
                    setText(null);
                } else {
                    setText(time.format(formatter));
                }
            }
        });
        clmID.setMaxWidth(100);
        clmID.setMinWidth(60);
        clmAction.setResizable(false);
        clmAction.setMinWidth(120);
        clmAction.setMaxWidth(120);
    }

    private void buttonDeleteAction(int productId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, DELETE_ALERT_TEXT, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            if(!productService.deleteProductByID(productId)){
                AlertUtil.permissionAlert().showAndWait();
            }
        }
    }

    public void btnNewProductAction(ActionEvent event) {
        btnNewProductCreation();
        popUpWindowSetup(event, appProperties.getProperty("newproducttitle"));
        paginationSetup();
        //loadData();
    }

    private Node createPage(int pageIndex) {
        ObservableList<Product> list= productService.getProductList(pageIndex,rowsPerPage);

        tableProduct.setItems(list);
        return new BorderPane(tableProduct);
    }

    private void btnNewProductCreation() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxlmProperties.getProperty("addproduct")));
        try {
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root, Double.valueOf(appProperties.getProperty("popupwidth")), Double.valueOf(appProperties.getProperty("popupheight"))));
            if (loader.getController() instanceof AddProductController) {
                AddProductController addProductController = loader.getController();
                addProductController.setProductService(productService);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buttonInfoAction(int id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxlmProperties.getProperty("infoproduct")));
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root, Double.valueOf(appProperties.getProperty("popupwidth")), Double.valueOf(appProperties.getProperty("popupheight"))));
            Product product = productService.getProductById(id);
            if (product != null) {
                if (loader.getController() instanceof ProductInfoController) {
                    ProductInfoController productInfoController = loader.getController();
                    productInfoController.setFields(product);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, PRODUCTNOTEXIST_ALERT);
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void buttonLogOutAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxlmProperties.getProperty("loginpage")));
        Parent root = loader.load();
        pane.getScene().setRoot(root);
    }

    public void btnBackAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event, fxlmProperties.getProperty("homepage"));
    }

    private void popUpWindowSetup(ActionEvent event, String windowTitle) {
        fxmlControllerStage.setTitle(windowTitle);
        fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
        fxmlControllerStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        fxmlControllerStage.setResizable(false);
        fxmlControllerStage.showAndWait();
    }

    private void updateButtonAction(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxlmProperties.getProperty("updateproduct")));
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root, Double.valueOf(appProperties.getProperty("popupwidth")), Double.valueOf(appProperties.getProperty("popupheight"))));
            if (loader.getController() instanceof UpdateProductController) {
                UpdateProductController updateProductController = loader.getController();
                updateProductController.setFields(product);
                updateProductController.setProductService(productService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
