package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.enums.OrderType;
import sample.model.Order;
import sample.model.OrderProduct;
import sample.repository.impl.OrderProductImpl;
import sample.service.OrderProductService;
import sample.utils.TableCellStyleUtil;

import java.math.BigDecimal;

public class InfoOrderController {

    @FXML
    private Button buttonClose;
    @FXML
    private TextField fieldCustomerName;
    @FXML
    private TextField fieldCustomerAddress;
    @FXML
    private TextField fieldDescription;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<OrderProduct, Integer> columnId;
    @FXML
    private TableColumn<OrderProduct, String> columnProduct;
    @FXML
    private TableColumn<OrderProduct, Integer> columnQuantity;
    @FXML
    private TableColumn<OrderProduct, Float> columnPrice;
    @FXML
    private TableColumn<OrderProduct, BigDecimal> columnTotalPrice;
    @FXML
    private TableColumn<OrderProduct, Float> columnDiscount;
    @FXML
    private Label labelSum;
    @FXML
    private Label labelDiscount;
    @FXML
    private ComboBox<OrderType> comboOrderType;

    public void closeButtonAction() {
        Stage stage = (Stage) buttonClose.getScene().getWindow();
        stage.close();
    }

    void setFields(Order order) {
        fieldCustomerName.setText(order.getCustomerName());
        fieldCustomerAddress.setText(order.getCustomerAddress());
        fieldDescription.setText(order.getDescription());
        comboOrderType.setItems(FXCollections.observableArrayList(order.getOrderType()));
        populateTable(order.getTransactionID());
    }

    private void populateTable(int orderId){
        OrderProductService orderProductService=new OrderProductService(new OrderProductImpl());
        ObservableList list=orderProductService.getOrderProductByOrderId(orderId);
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        columnDiscount.setCellFactory(tc -> TableCellStyleUtil.setMonetaryColumnStyle());
        columnPrice.setCellFactory(tc -> TableCellStyleUtil.setMonetaryColumnStyle());
        columnTotalPrice.setCellFactory(tc -> TableCellStyleUtil.setMonetaryColumnStyle());
        tableView.setItems(list);
    }
}
