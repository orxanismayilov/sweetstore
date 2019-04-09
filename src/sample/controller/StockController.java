package sample.controller;

import javafx.application.Platform;
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
import sample.model.Product;
import sample.service.ProductService;
import sample.utils.ScreenUtils;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private ProductService productService;
    private Stage fxmlControllerStage;

    private final static String FXML_URL_HOMEPAGE="../resource/screens/homepage.fxml";
    private final static String FXML_URL_LOGINPAGE="../resource/screens/loginpage.fxml";
    private final static String FXML_URL_NEWPRODUCT="../resource/screens/addproduct.fxml";
    private final static String FXML_URL_PRODUCTINFO="/sample/resource/screens/productinfopage.fxml";
    private final static String FXML_URL_UPDATEPRODUCT="/sample/resource/screens/updateproduct.fxml";
    private final static String INFO_TITLE="Info";
    private final static String UPDATE_TITLE="Update product";
    private final static String NEW_PRODUCT_TITLE="New product";
    private final static String PRODUCTNOTEXIST_ALERT="Product not exist";
    private final static String DELETE_ALERT_TEXT="Are you sure ?";
    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static final Image imageUpdate = new Image("/sample/resource/images/edit_property_26px.png");
    private static final Image imageInfo = new Image("/sample/resource/images/info_24px.png");

    @FXML private TableView <Product> tableProduct;
    @FXML private TableColumn<Product,Integer> clmID;
    @FXML private TableColumn<Product,String> clmName;
    @FXML private TableColumn<Product,Float> clmPrice;
    @FXML private TableColumn<Product,LocalDateTime> clmLastUpdate;
    @FXML private TableColumn<Product,Integer> clmQuantity;
    @FXML private TableColumn<Product,Void> clmAction;
    @FXML BorderPane pane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService=new ProductService();
        createTable();
        loadData();
    }

    private void createTable() {
        clmID.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmLastUpdate.setCellValueFactory(new PropertyValueFactory<>("updateDate"));
        clmQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        clmPrice.setCellFactory(tc -> new TableCell<Product, Float>() {
            private final Label labelSign = new Label();
            private final Label labelPrice = new Label();
            @Override
            protected void updateItem(Float price, boolean empty) {
                super.updateItem(price, empty);
                NumberFormat numberFormat = NumberFormat.getInstance();
                labelSign.setText("\u20BC");
                HBox pane = new HBox(labelPrice, labelSign);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                if (empty) {
                    setGraphic(null);
                } else {
                    labelPrice.setText(numberFormat.format(price));
                    setGraphic(pane);
                }
            }
        });
        clmAction.setCellFactory(tc->new TableCell<Product,Void>(){
            final ImageView buttonDeleteGraphic = new ImageView();
            final ImageView buttonUpdateGraphic = new ImageView();
            final ImageView buttonInfoGraphic= new ImageView();
            private final Button buttonDelete = new Button();
            private final Button buttonUpdate=new Button();
            private final Button buttonInfo=new Button();
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
                HBox pane=new HBox(buttonDelete,buttonUpdate,buttonInfo);
                pane.setSpacing(10);
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);

                    buttonDelete.setOnAction((ActionEvent eventDelete) -> {
                        Product selectedProduct=(Product) getTableRow().getItem();
                        buttonDeleteAction(selectedProduct.getId());
                    });

                    buttonInfo.setOnAction((ActionEvent eventInfo)->{
                        Product product= (Product) getTableRow().getItem();
                        buttonInfoAction(product.getId());
                        popUpWindowSetup(eventInfo,INFO_TITLE);
                    });

                    buttonUpdate.setOnAction((ActionEvent eventUpdate)->{
                       Product product=(Product) getTableRow().getItem();
                       updateButtonAction(product);
                       popUpWindowSetup(eventUpdate,UPDATE_TITLE);
                    });
                }
            }
        });
        clmLastUpdate.setCellFactory(tc->new TableCell<Product,LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime time,boolean empty){
                super.updateItem(time,empty);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                if (empty){
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

    private void loadData(){
        ObservableList data=productService.getProductList();
        tableProduct.setItems(data);
    }

    private void buttonDeleteAction(int productId){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,DELETE_ALERT_TEXT , ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            productService.deleteProductbyID(productId);
            loadData();
        }
    }

    public void btnNewProductAction(ActionEvent event) {
        btnNewProductCreation(event);
        loadData();
    }

    private void btnNewProductCreation(ActionEvent event){

        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_NEWPRODUCT));
        try{
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root,500,300));
            if(loader.getController() instanceof AddProductController){
                AddProductController addProductController = loader.getController();
                addProductController.setStage(fxmlControllerStage);
                addProductController.setProductService(productService);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        fxmlControllerStage.setTitle(NEW_PRODUCT_TITLE);
        fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
        fxmlControllerStage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        fxmlControllerStage.setResizable(false);
        fxmlControllerStage.showAndWait();
    }

    private void buttonInfoAction(int id){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_PRODUCTINFO));
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
            Product product=productService.getProductById(id);
            if(product!=null){
                if(loader.getController() instanceof ProductInfoController){
                    ProductInfoController productInfoController = loader.getController();
                    productInfoController.setStage(fxmlControllerStage);
                    productInfoController.setProduct(product);
                    productInfoController.setFileds();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR, PRODUCTNOTEXIST_ALERT);
                alert.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void buttonLogOutAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_LOGINPAGE));
        Parent root = loader.load();
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void btnBackAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event, FXML_URL_HOMEPAGE);
    }

    private void popUpWindowSetup(ActionEvent event,String windowTitle){
        fxmlControllerStage.setTitle(windowTitle);
        fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
        fxmlControllerStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        fxmlControllerStage.setResizable(false);
        fxmlControllerStage.show();
        fxmlControllerStage.setOnHiding(event1 -> Platform.runLater(() -> tableProduct.refresh()));
    }

    private void updateButtonAction(Product product){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_UPDATEPRODUCT));
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
            if(loader.getController() instanceof UpdateProductController){
                UpdateProductController updateProductController = loader.getController();
                updateProductController.setStage(fxmlControllerStage);
                updateProductController.setProduct(product);
                updateProductController.setFields();
                updateProductController.setProductService(productService);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
