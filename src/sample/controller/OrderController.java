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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.enums.OrderStatus;
import sample.model.Order;
import sample.enums.OrderType;
import sample.service.OrderService;
import sample.utils.TableCellStyleUtil;
import sample.utils.ScreenUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    private OrderService orderService;
    private Stage fxmlControllerStage;
    private Properties fxmlProperties;
    private Properties appProperties;

    private final static String FXML_PROPERTIES_URL = "sample/resource/properties/fxmlurls.properties";
    private final static String APP_PROPERTIES_URL = "sample/resource/properties/application.properties";
    private final static String DATE_PATTERN="yyyy-MM-dd HH:mm:ss";
    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static final Image imageUpdate = new Image("/sample/resource/images/edit_property_26px.png");
    private static final Image imageInfo = new Image("/sample/resource/images/info_24px.png");

    @FXML private TableView<Order> tableView;
    @FXML private TableColumn<Order, String> clmName;
    @FXML private TableColumn<Order, String> clmAddress;
    @FXML private TableColumn<Order,BigDecimal> clmTotalprice;
    @FXML private TableColumn<Order,OrderType> clmOrdertype;
    @FXML private TableColumn<Order,Integer> clmTransactionID;
    @FXML private TableColumn<Order,StringBuilder > clmDescription;
    @FXML private TableColumn<Order,LocalDateTime>clmDate;
    @FXML private TableColumn<Order, Void> clmAction;
    @FXML private TableColumn<Order, OrderStatus> clmOrderStatus;
    @FXML BorderPane pane;
    @FXML Button btnNewOrder;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderService=new OrderService();
        tableBinding();
        loadTable();
        loadPropertiesFile();
    }

    public void addOrder(ActionEvent event) {
        addOrderCreation();
        popUpWindowSetup(event,appProperties.getProperty("newordertitle"));
        loadTable();
    }

    public void buttonLogOutAction() throws IOException {
        FXMLLoader loader =new FXMLLoader(getClass().getResource(fxmlProperties.getProperty("loginpage")));
        Parent root = loader.load();
        pane.getScene().setRoot(root);
    }

    public void backButtonAction(ActionEvent event) throws Exception{
        ScreenUtils.changeScreen(event, fxmlProperties.getProperty("homepage"));
    }

    private void loadTable(){
        ObservableList<Order> list=orderService.getOrderList();
        tableView.setItems(list);
    }

    private void addOrderCreation() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlProperties.getProperty("neworder")));
        try{
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void tableBinding(){
        clmName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        clmAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        clmDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        clmTotalprice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        clmOrdertype.setCellValueFactory(new PropertyValueFactory<>("orderType"));
        clmTransactionID.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        clmOrderStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
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
                        Order order=(Order) getTableRow().getItem();
                        buttonDeleteAction(order);
                        loadTable();
                    });

                    buttonInfo.setOnAction((ActionEvent eventInfo)->{
                        Order order=(Order) getTableRow().getItem();
                        buttonInfoAction(order);
                        popUpWindowSetup(eventInfo,appProperties.getProperty("infoordertitle"));
                    });

                    buttonUpdate.setOnAction((ActionEvent event)->{
                        Order order=(Order) getTableRow().getItem();
                        buttonUpdateAction(order);
                        popUpWindowSetup(event,appProperties.getProperty("updateordertitle"));
                        loadTable();
                    });
                }
            }
        });
        clmAction.setResizable(false);
        clmAction.setMinWidth(120);
        clmAction.setMaxWidth(120);
        clmTotalprice.setCellFactory(tc->TableCellStyleUtil.setMonetaryColumnStyle());
        clmOrderStatus.setCellFactory(tc-> new TableCell<Order,OrderStatus>(){
            @Override
            protected void updateItem(OrderStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    if (item==OrderStatus.PENDDING) {
                        setTextFill(Color.BLACK);
                        setStyle("-fx-font-weight: bold");
                        setStyle("-fx-background-color: yellow");
                        setText(item.getEngMeaning());
                    } else if(item==OrderStatus.DELIVERED){
                        setTextFill(Color.BLACK);
                        setStyle("-fx-font-weight: bold");
                        setStyle("-fx-background-color: green");
                        setText(item.getEngMeaning());
                    } else {
                        setTextFill(Color.BLACK);
                        setStyle("-fx-font-weight: bold");
                        setStyle("-fx-background-color: red");
                        setText(item.getEngMeaning());
                    }
                }
            }
        });

        clmDate.setCellFactory(tc-> new TableCell<Order, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

    }

    private void buttonDeleteAction(Order order){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure ?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            int id=order.getTransactionID();
            orderService.deleteOrderByTransactionId(id);
        }
    }

    private void buttonUpdateAction(Order order){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlProperties.getProperty("updateorder")));
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
            if(loader.getController() instanceof UpdateOrderController){
                UpdateOrderController updateOrderController = loader.getController();
                updateOrderController.setFields(order);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void buttonInfoAction (Order order){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlProperties.getProperty("infoproduct")));
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
            if(loader.getController() instanceof OrderInfoController){
             /*   OrderInfoController orderInfoController = loader.getController();
                orderInfoController.setStage(fxmlControllerStage);
                orderInfoController.setOrder(order);
                orderInfoController.setFileds();*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void popUpWindowSetup(ActionEvent event,String windowTitle){
        fxmlControllerStage.setTitle(windowTitle);
        fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
        fxmlControllerStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        fxmlControllerStage.setResizable(false);
        fxmlControllerStage.showAndWait();
    }

    private void loadPropertiesFile() {
        try {
            InputStream fxmlInput = HomeController.class.getClassLoader().getResourceAsStream(FXML_PROPERTIES_URL);
            InputStream appInput =  HomeController.class.getClassLoader().getResourceAsStream(APP_PROPERTIES_URL);
            fxmlProperties = new Properties();
            appProperties = new Properties();
            appProperties.load(appInput);
            fxmlProperties.load(fxmlInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
