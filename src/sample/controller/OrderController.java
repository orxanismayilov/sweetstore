package sample.controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.Order;
import sample.model.Product;
import sample.service.OrderService;
import sample.utils.ScreenUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    private OrderService orderService;
    private final static String FXML_URL_HOMEPAGE="../resource/screens/homepage.fxml";
    private final static String FXML_URL_NEWORDER= "/sample/resource/screens/neworder.fxml";
    private final static String FXML_URL_PRODUCTINFO="/sample/resource/screens/productinfopage.fxml";
    private final static String FXML_URL_LOGINPAGE="/sample/resource/screens/loginpage.fxml";
    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static final Image imageUpdate = new Image("/sample/resource/images/edit_property_26px.png");
    private static final Image imageInfo = new Image("/sample/resource/images/info_24px.png");

    private Stage fxmlControllerStage;
    @FXML private TableView<Order> tableView;
    @FXML private TableColumn<Order, String> clmName;
    @FXML private TableColumn<Order, String> clmAddress;
    @FXML private TableColumn<Order,BigDecimal> clmTotalprice;
    @FXML private TableColumn<Order,String> clmOrdertype;
    @FXML private TableColumn<Order,Integer> clmTransactionID;
    @FXML private TableColumn<Order,StringBuilder > clmDescription;
    @FXML private TableColumn<Order,LocalDate>clmDate;
    @FXML private TableColumn<Order, Void> clmAction;
    @FXML private BorderPane pane;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderService=new OrderService();
        tableBinding();
        tableView.setItems(orderService.getData());
    }

    public void addOrder(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_NEWORDER));
        try{
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
        }catch(IOException e){
            e.printStackTrace();
        }
        fxmlControllerStage.setTitle("Update");
        fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
        fxmlControllerStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        fxmlControllerStage.setResizable(false);
        fxmlControllerStage.show();
    }

    public void buttonLogOutAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_LOGINPAGE));
        Parent root = loader.load();
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void backButtonAction(ActionEvent event) throws Exception{
       ScreenUtils.changeScreen(event,FXML_URL_HOMEPAGE);
    }

    private void tableBinding(){
        clmName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        clmAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        clmDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        clmTotalprice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        clmOrdertype.setCellValueFactory(new PropertyValueFactory<>("orderType"));
        clmTransactionID.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmAction.setResizable(false);
        clmAction.setMinWidth(120);
        clmAction.setMaxWidth(120);
        clmAction.setCellFactory(tc->new TableCell<Order,Void>(){
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
                            orderService.deleteOrderByTransactionId(clmTransactionID.getCellData(getTableRow().getIndex()));
                            tableBinding();
                        }

                    });

                    buttonInfo.setOnAction((ActionEvent eventInfo)->{
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_URL_PRODUCTINFO));
                            Parent root = loader.load();
                            fxmlControllerStage = new Stage();
                            fxmlControllerStage.setScene(new Scene(root));
                            ObservableList<Product> list =orderService.getData();
                            Integer value= clmTransactionID.getCellData(getTableRow().getIndex());
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
                            fxmlControllerStage.setTitle("Update");
                            fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
                            fxmlControllerStage.initOwner(((Node)eventInfo.getSource()).getScene().getWindow());
                            fxmlControllerStage.setResizable(false);
                            fxmlControllerStage.show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    });
                }
            }
        });
        clmAction.setResizable(false);
        clmAction.setMinWidth(120);
        clmAction.setMaxWidth(120);
        clmTotalprice.setCellFactory(tc -> new TableCell<Order, BigDecimal>() {
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

}
