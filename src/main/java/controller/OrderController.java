package controller;

import dtos.OrdersDTO;
import enums.OrderStatus;
import enums.OrderType;
import enums.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Order;
import model.UserSession;
import repository.impl.OrderDaoImpl;
import service.OrderService;
import service.serviceImpl.OrderServiceImpl;
import utils.AlertUtil;
import utils.LoadPropertyUtil;
import utils.ScreenUtils;
import utils.TableCellStyleUtil;

import java.awt.*;
import java.io.IOException;
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
    private PseudoClass pendigPseudoClass=PseudoClass.getPseudoClass("editable");
    private PseudoClass deliveredPseudoClass=PseudoClass.getPseudoClass("readonly");
    private PseudoClass closedPseudoClass=PseudoClass.getPseudoClass("disabled");
    private UserSession userSession;

    private final static String FXML_PROPERTIES_URL = "/resources/properties/fxmlurls.properties";
    private final static String APP_PROPERTIES_URL = "/resources/properties/application.properties";
    private String ALERT_TEXT = "Are you sure ?";
    private static final   int rowsPerPage=10;
    private TableView<Order> tableView;
    private Pagination pages ;

    @FXML
    private BorderPane pane;
    @FXML
    private TextField searchBox;
    @FXML
    private CheckBox checkBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderService = new OrderServiceImpl();
        this.userSession=UserSession.getInstance();
        tableBinding();
        createPagination();
        fxmlProperties = LoadPropertyUtil.loadPropertiesFile(FXML_PROPERTIES_URL);
        appProperties = LoadPropertyUtil.loadPropertiesFile(APP_PROPERTIES_URL);
    }

    public void createPagination() {
        pages=new Pagination(1,0);
        pages.setPageFactory(pageIndex -> {
            OrdersDTO data = orderService.getOrderList(pageIndex, rowsPerPage);
            int numOfPages = 1;
            int listSize = data.getCount();
            if (listSize % rowsPerPage == 0) {
                numOfPages = listSize / rowsPerPage;
            } else if (listSize > rowsPerPage) {
                numOfPages = listSize / rowsPerPage + 1;
            }
            pages.setPageCount(numOfPages);
            tableView.setItems(FXCollections.observableArrayList(data.getOrders()));
            return tableView;
        });
        pane.centerProperty().setValue(pages);
    }

   /* private Node createPage(Integer pageIndex) {
        ObservableList list= orderService.getOrderList(pageIndex,rowsPerPage);
        tableView.setItems(list);
        return tableView;
    }*/

    public void addOrder(ActionEvent event) {
        addOrderCreation();
        popUpWindowSetup(event, appProperties.getProperty("newordertitle"));
        createPagination();
        //loadTable();
    }

    public void buttonLogOutAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlProperties.getProperty("loginpage")));
        Parent root = loader.load();
        pane.getScene().setRoot(root);
    }

    public void backButtonAction(ActionEvent event) throws Exception {
        ScreenUtils.changeScreen(event, fxmlProperties.getProperty("homepage"));
    }

    public void searchButtonAction() {
        boolean isSelected=checkBox.isSelected();
        if (isSelected){
           if (userSession.getUser().getRole()== UserRole.USER) {
               AlertUtil.permissionAlert().showAndWait();
           } else {
               tableView.setItems(FXCollections.observableArrayList(orderService.searchOrderById(searchBox.getText(), isSelected)));
           }
        } else {
            tableView.setItems(FXCollections.observableArrayList(orderService.searchOrderById(searchBox.getText(), isSelected)));
        }
    }

    private void addOrderCreation() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlProperties.getProperty("neworder")));
        try {
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tableBinding() {
        tableView=new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Order, String> clmName = new TableColumn<>("Customer Name");
        TableColumn<Order, String> clmAddress = new TableColumn<>("Customer Address");
        TableColumn<Order, StringBuilder> clmDescription = new TableColumn<>("Description");
        TableColumn<Order, OrderType> clmOrdertype = new TableColumn<>("Order Type");
        TableColumn<Order, Integer> clmTransactionID = new TableColumn<>("Transaction Id");
        TableColumn<Order, OrderStatus> clmOrderStatus = new TableColumn<>("Order Status");
        TableColumn<Order, String> clmDate = new TableColumn<>("Date");
        TableColumn<Order, Void> clmAction = new TableColumn<>("Action");
        TableColumn<Order, BigDecimal> clmTotalprice = new TableColumn<>("Total Price");

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
        clmAction.setCellFactory(tc -> new TableCell<Order, Void>() {
            final ImageView buttonDeleteGraphic = new ImageView();
            final ImageView buttonUpdateGraphic = new ImageView();
            final ImageView buttonInfoGraphic = new ImageView();
            private final Button buttonDelete = new Button();
            private final Button buttonUpdate = new Button();
            private final Button buttonInfo = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                Image imageDelete = new Image(appProperties.getProperty("imageDelete"));
                Image imageUpdate = new Image(appProperties.getProperty("imageUpdate"));
                Image imageInfo = new Image(appProperties.getProperty("imageInfo"));
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
                        Order order = (Order) getTableRow().getItem();
                        buttonDeleteAction(order);
                    });

                    buttonInfo.setOnAction((ActionEvent eventInfo) -> {
                        Order order = (Order) getTableRow().getItem();
                        buttonInfoAction(order);
                        popUpWindowSetup(eventInfo, appProperties.getProperty("infoordertitle"));
                    });

                    buttonUpdate.setOnAction((ActionEvent event) -> {
                        Order order = (Order) getTableRow().getItem();
                        buttonUpdateAction(order);
                        popUpWindowSetup(event, appProperties.getProperty("updateordertitle"));
                        createPagination();
                    });
                }
            }
        });
        clmAction.setResizable(false);
        clmAction.setMinWidth(120);
        clmAction.setMaxWidth(120);
        clmTotalprice.setCellFactory(tc -> TableCellStyleUtil.setMonetaryColumnStyle());
        clmOrderStatus.setCellFactory(tc -> new TableCell<Order, OrderStatus>() {
            @Override
            protected void updateItem(OrderStatus item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    if (item == OrderStatus.PENDDING) {
                        pseudoClassStateChanged(pendigPseudoClass, true);
                        setText(item.getEngMeaning());
                    } else if (item == OrderStatus.DELIVERED) {
                        pseudoClassStateChanged(deliveredPseudoClass, true);
                        setText(item.getEngMeaning());
                    } else {
                        pseudoClassStateChanged(closedPseudoClass, true);
                        setText(item.getEngMeaning());
                    }
                } else {
                    setText(null);
                    pseudoClassStateChanged(pendigPseudoClass, false);
                    pseudoClassStateChanged(deliveredPseudoClass, false);
                    pseudoClassStateChanged(closedPseudoClass, false);
                }
            }
        });

        tableView.getColumns().addAll(clmTransactionID, clmName, clmAddress, clmDescription, clmTotalprice, clmOrdertype, clmOrderStatus, clmDate, clmAction);

    }

    private void buttonDeleteAction(Order order) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, ALERT_TEXT, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            int id = order.getTransactionID();
            if (orderService.deleteOrderByTransactionId(id)) {
                createPagination();
            } else {
                AlertUtil.permissionAlert().showAndWait();
            }

        }
    }

    private void buttonUpdateAction(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlProperties.getProperty("updateorder")));
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
            if (loader.getController() instanceof UpdateOrderController) {
                UpdateOrderController updateOrderController = loader.getController();
                updateOrderController.setFields(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonInfoAction(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlProperties.getProperty("infoorder")));
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
            InfoOrderController infoOrderController = loader.getController();
            infoOrderController.setFields(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popUpWindowSetup(ActionEvent event, String windowTitle) {
        fxmlControllerStage.setTitle(windowTitle);
        fxmlControllerStage.initModality(Modality.WINDOW_MODAL);
        fxmlControllerStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        fxmlControllerStage.setResizable(false);
        fxmlControllerStage.showAndWait();
    }
}
