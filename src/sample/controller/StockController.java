package sample.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import sample.model.Product;
import sample.service.ProductService;
import sample.utils.ScreenUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private ProductService productService;
    private Stage fxmlControllerStage;
    private ActionEvent event;

    private final static String FXML_URL_HOMEPAGE="../resource/screens/homepage.fxml";
    private final static String FXML_URL_LOGINPAGE="../resource/screens/loginpage.fxml";
    private final static String FXML_URL_NEWPRODUCT="../resource/screens/addproduct.fxml";
    private final static String FXML_URL_PRODUCTINFO="/sample/resource/screens/productinfopage.fxml";
    private final static String FXML_URL_UPDATEPRODUCT="/sample/resource/screens/updateproduct.fxml";
    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static final Image imageUpdate = new Image("/sample/resource/images/edit_property_26px.png");
    private static final Image imageInfo = new Image("/sample/resource/images/info_24px.png");

    @FXML private TableView <Product> tableProduct;
    @FXML private TableColumn<Product,Integer> clmID;
    @FXML private TableColumn<Product,String> clmName;
    @FXML private TableColumn<Product,BigDecimal> clmPrice;
    @FXML private TableColumn<Product,LocalDate> clmLastUpdate;
    @FXML private TableColumn<Product,Integer> clmQuantity;
    @FXML private TableColumn<Product,Void> clmAction;
    @FXML private BorderPane pane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService=new ProductService();
        populateTable();
        loadData();
    }

    private void populateTable() {
        clmID.setCellValueFactory(new PropertyValueFactory<>("id"));
        clmPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clmLastUpdate.setCellValueFactory(new PropertyValueFactory<>("updateDate"));
        clmQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
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
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure ?", ButtonType.YES, ButtonType.CANCEL);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.YES) {
                            productService.deleteProductbyID(clmID.getCellData(getTableRow().getIndex()));
                            loadData();
                        }

                    });

                    buttonInfo.setOnAction((ActionEvent eventInfo)->{
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_PRODUCTINFO));
                            Parent root = loader.load();
                            fxmlControllerStage = new Stage();
                            fxmlControllerStage.setScene(new Scene(root));
                            ObservableList<Product> list = productService.getData();
                            Integer value= clmID.getCellData(getTableRow().getIndex());
                            for (Product product : list) {
                                if (product.getId()==value) {
                                    if(loader.getController() instanceof ProductInfoController){
                                        ProductInfoController productInfoController = loader.getController();
                                        productInfoController.setStage(fxmlControllerStage);
                                        productInfoController.setProduct(product);
                                        productInfoController.setFileds();
                                    }
                                }
                            }
                            fxmlControllerStage.setTitle("Info");
                            fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
                            fxmlControllerStage.initOwner(((Node)eventInfo.getSource()).getScene().getWindow());
                            fxmlControllerStage.setResizable(false);
                            fxmlControllerStage.show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    });

                    buttonUpdate.setOnAction((ActionEvent eventUpdate)->{
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_UPDATEPRODUCT));
                            Parent root = loader.load();
                            fxmlControllerStage = new Stage();
                            fxmlControllerStage.setScene(new Scene(root));
                            ObservableList<Product> list = productService.getData();
                            Integer value= clmID.getCellData(getTableRow().getIndex());
                            for (Product product : list) {
                                if (product.getId()==value) {
                                    if(loader.getController() instanceof UpdateProductController){
                                        UpdateProductController updateProductController = loader.getController();
                                        updateProductController.setStage(fxmlControllerStage);
                                        updateProductController.setProduct(product);
                                        updateProductController.setFileds();
                                        updateProductController.setProductService(productService);
                                    }
                                }
                            }
                            fxmlControllerStage.setTitle("Update Product");
                            fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
                            fxmlControllerStage.initOwner(((Node)eventUpdate.getSource()).getScene().getWindow());
                            fxmlControllerStage.setResizable(false);
                            fxmlControllerStage.show();
                            fxmlControllerStage.setOnHiding(new EventHandler<WindowEvent>() {

                                @Override
                                public void handle(WindowEvent event) {
                                    Platform.runLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            tableProduct.refresh();
                                        }
                                    });
                                }
                            });
                            loadData();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
        clmID.setMaxWidth(100);
        clmID.setMinWidth(60);
        clmAction.setResizable(false);
        clmAction.setMinWidth(120);
        clmAction.setMaxWidth(120);
        clmPrice.setCellFactory(tc -> new TableCell<Product, BigDecimal>() {
            private final Label labelSign = new Label();
            private final Label labelPrice = new Label();
            @Override
            protected void updateItem(BigDecimal priceBigDecimal, boolean empty) {
                super.updateItem(priceBigDecimal, empty);
                NumberFormat numberFormat = NumberFormat.getInstance();
                labelSign.setText("\u20BC");
                AnchorPane pane = new AnchorPane(labelPrice, labelSign);
                AnchorPane.setLeftAnchor(labelPrice, 0.0);
                AnchorPane.setRightAnchor(labelSign, 0.0);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                if (empty) {
                    setGraphic(null);
                } else {
                    labelPrice.setText(numberFormat.format(priceBigDecimal));
                    setGraphic(pane);
                }
            }
        });
    }

    private void loadData(){
        ObservableList data=productService.getData();
        tableProduct.setItems(data);
    }

    public void btnNewProductAction(ActionEvent event) {
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

        fxmlControllerStage.setTitle("Update");
        fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
        fxmlControllerStage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        fxmlControllerStage.setResizable(false);
        fxmlControllerStage.showAndWait();
        loadData();
    }

    public void buttonLogOutAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_LOGINPAGE));
        Parent root = loader.load();
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void btnBackAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event, FXML_URL_HOMEPAGE);
    }
}
