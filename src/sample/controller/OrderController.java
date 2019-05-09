package sample.controller;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.enums.OrderStatus;
import sample.enums.OrderType;
import sample.model.Order;
import sample.service.OrderService;
import sample.utils.LoadPropertyUtil;
import sample.utils.ScreenUtils;
import sample.utils.TableCellStyleUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
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

    private final static String FXML_PROPERTIES_URL = "C:\\Users\\Orxan\\Desktop\\Home Project\\Home Project\\src\\sample\\resource\\properties\\fxmlurls.properties";
    private final static String APP_PROPERTIES_URL = "C:\\Users\\Orxan\\Desktop\\Home Project\\Home Project\\src\\sample\\resource\\properties\\application.properties";
    private final static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static final Image imageUpdate = new Image("/sample/resource/images/edit_property_26px.png");
    private static final Image imageInfo = new Image("/sample/resource/images/info_24px.png");
    private static final   int rowsPerPage=10;
    private TableView<Order> tableView;
    private TableColumn<Order, String> clmName;
    private TableColumn<Order, String> clmAddress;
    private TableColumn<Order, BigDecimal> clmTotalprice;
    private TableColumn<Order, OrderType> clmOrdertype;
    private TableColumn<Order, Integer> clmTransactionID;
    private TableColumn<Order, StringBuilder> clmDescription;
    private TableColumn<Order, LocalDateTime> clmDate;
    private TableColumn<Order, Void> clmAction;
    private TableColumn<Order, OrderStatus> clmOrderStatus;
    private Pagination pages;
    @FXML
    private BorderPane pane;
    @FXML
    private TextField searchBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderService = new OrderService();
        tableBinding();
        createPagination();
        fxmlProperties = LoadPropertyUtil.loadPropertiesFile(FXML_PROPERTIES_URL);
        appProperties = LoadPropertyUtil.loadPropertiesFile(APP_PROPERTIES_URL);
    }

    private void createPagination() {
        int numOfPages = 1;
        int listSize=orderService.getTotalCountOfOrder();
        if (orderService.getOrderList().size() % rowsPerPage == 0) {
            numOfPages = orderService.getOrderList().size() / rowsPerPage;
        } else if (orderService.getOrderList().size() > rowsPerPage) {
            numOfPages = orderService.getOrderList().size() / rowsPerPage + 1;
        }
        pages=new Pagination(numOfPages,0);
        pages.setPageFactory(this::createPage);
        pane.centerProperty().setValue(pages);
    }

    private Node createPage(Integer pageIndex) {
        ObservableList<Order> list=orderService.getOrderList();
        int fromIndex=rowsPerPage*pageIndex;
        int toIndex=Math.min(fromIndex+rowsPerPage,list.size());
        tableView.setItems(FXCollections.observableArrayList(list.subList(fromIndex,toIndex)));
        return tableView;
    }

    public void addOrder(ActionEvent event) {
        addOrderCreation();
        popUpWindowSetup(event, appProperties.getProperty("newordertitle"));
        loadTable();
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
        ObservableList<Order> list=orderService.searchOrderById(searchBox.getText());
        tableView.setItems(list);
    }

    public void loadTable() {
        ObservableList<Order> list = orderService.getOrderList();
        tableView.setItems(list);
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
        clmName=new TableColumn<>("Customer Name");
        clmAddress=new TableColumn<>("Customer Address");
        clmDescription=new TableColumn<>("Description");
        clmOrdertype=new TableColumn<>("Order Type");
        clmTransactionID=new TableColumn<>("Transaction Id");
        clmOrderStatus=new TableColumn<>("Order Status");
        clmDate=new TableColumn<>("Date");
        clmAction=new TableColumn<>("Action");
        clmTotalprice=new TableColumn<>("Total Price");

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
                        try {
                            buttonDeleteAction(order);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        loadTable();
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
                        loadTable();
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
                        pseudoClassStateChanged(pendigPseudoClass,true);
                        setText(item.getEngMeaning());
                    } else if (item == OrderStatus.DELIVERED) {
                        pseudoClassStateChanged(deliveredPseudoClass,true);
                        setText(item.getEngMeaning());
                    } else {
                        pseudoClassStateChanged(closedPseudoClass,true);
                        setText(item.getEngMeaning());
                    }
                } else {
                    setText(null);
                    pseudoClassStateChanged(pendigPseudoClass,false);
                    pseudoClassStateChanged(deliveredPseudoClass,false);
                    pseudoClassStateChanged(closedPseudoClass,false);
                }
            }
        });

        clmDate.setCellFactory(tc -> new TableCell<Order, LocalDateTime>() {
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

        tableView.getColumns().addAll(clmTransactionID,clmName,clmAddress,clmDescription,clmTotalprice,clmOrdertype,clmOrderStatus,clmDate,clmAction);

    }

    private void buttonDeleteAction(Order order) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure ?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            int id = order.getTransactionID();
            orderService.deleteOrderByTransactionId(id);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlProperties.getProperty("infoproduct")));
            Parent root = loader.load();
            fxmlControllerStage = new Stage();
            fxmlControllerStage.setScene(new Scene(root));
            if (loader.getController() instanceof OrderInfoController) {
             /*   OrderInfoController orderInfoController = loader.getController();
                orderInfoController.setStage(fxmlControllerStage);
                orderInfoController.setOrder(order);
                orderInfoController.setFileds();*/
            }
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
