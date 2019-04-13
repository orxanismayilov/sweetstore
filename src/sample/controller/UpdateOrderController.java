package sample.controller;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.model.*;
import sample.service.OrderProductService;
import sample.service.OrderService;
import sample.service.ProductService;
import sample.utils.TableCellStyleUtil;
import sample.utils.NumberUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class UpdateOrderController implements Initializable {

    private OrderProduct orderProduct;
    private ProductService productService;
    private OrderProductService orderProductService;
    private Order order;
    private Product product;
    private OrderService orderService;
    private OrderProductSummary summary;

    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static String ALERT_TEXT = "Please enter valid input!";
    private static String MANAT_SYMBOL="\u20BC";
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");


    @FXML
    private ComboBox comboBoxProducts;
    @FXML
    private ComboBox<OrderType> comboOrderType;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonClose;
    @FXML
    private TextField fieldCustomerName;
    @FXML
    private TextField fieldCustomerAddress;
    @FXML
    private TextField fieldQuantity;
    @FXML
    private TextField fieldPrice;
    @FXML
    private TextField fieldTotalPrice;
    @FXML
    private TextField fieldDiscount;
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
    private TableColumn<OrderProduct, Void> columnAction;
    @FXML
    private Label labelAlert;
    @FXML
    private Label labelPossibleQuantity;
    @FXML
    private Label labelDescription;
    @FXML
    private Label labelSum;
    @FXML
    private Label labelDiscount;
    private int orderId;
    private Stage stage;

    private void populateTable() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        columnPrice.setCellFactory(tc ->TableCellStyleUtil.setMonetaryColumnStyle());

        columnDiscount.setCellFactory(tc->TableCellStyleUtil.setMonetaryColumnStyle());

        columnTotalPrice.setCellFactory(tc ->TableCellStyleUtil.setMonetaryColumnStyle());

        columnAction.setCellFactory(tc -> new TableCell<OrderProduct, Void>() {
            final ImageView buttonDeleteGraphic = new ImageView();
            private final Button buttonDelete = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                buttonDeleteGraphic.setImage(imageDelete);
                buttonDeleteGraphic.setFitWidth(12);
                buttonDeleteGraphic.setFitHeight(12);
                buttonDelete.setGraphic(buttonDeleteGraphic);
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonDelete);

                    buttonDelete.setOnAction((ActionEvent eventDelete) -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure ?", ButtonType.YES, ButtonType.CANCEL);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.YES) {
                            orderProductService.removeOrderProductByProductId(columnId.getCellData(getTableRow().getIndex()));
                            loadTable();
                            clearFields();
                            summary.fillDescriptionCalculateTotalPriceAndDiscount(orderId);
                            labelDescription.setText(String.valueOf(summary.getDescription()));
                        }
                    });
                }
            }
        });
    }

    public void addButtonAction() {
        if (buttonAdd.getText().equals("ADD")) {
            addOrderProduct();
        } else {
            updateOrderProduct();
        }
    }

    public void comboBoxAction() {
        Product product = productService.getProductByName(String.valueOf(comboBoxProducts.getValue()));
        if (product != null) {
            setProduct(product);
            fieldPrice.setText(String.valueOf(product.getPrice()));
            labelPossibleQuantity.setText(String.valueOf(product.getQuantity()));
        }
    }

    public void saveButtonAction() {
        summary.fillDescriptionCalculateTotalPriceAndDiscount(orderId);
        order.setCustomerName(fieldCustomerName.getText());
        order.setCustomerAddress(fieldCustomerAddress.getText());
        order.setDescription(summary.getDescription());
        order.setOrderType(comboOrderType.getValue());
        orderService.addNewOrderToList(order);
        Stage stage = (Stage) buttonSave.getScene().getWindow();
        stage.close();
    }

    public void closeButtonAction() {
        Stage stage = (Stage) buttonClose.getScene().getWindow();
        stage.close();
    }

    private void addOrderProduct() {
        try {
            String productName = String.valueOf(comboBoxProducts.getValue());
            Product product = productService.getProductByName(productName);
            setProduct(product);
            orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getTransactionID());
            orderProduct.setProductId(product.getId());
            orderProduct.setProductName(product.getName());
            orderProduct.setProductQuantity(Integer.parseInt(fieldQuantity.getText()));
            orderProduct.setProductPrice(product.getPrice());
            orderProduct.setTotalPrice(new BigDecimal(Double.toString(product.getPrice())).multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText())));
            orderProduct.setDiscount(Float.parseFloat(fieldDiscount.getText()));
            order.setTotalPrice(order.getTotalPrice().add(orderProduct.getTotalPrice()));
            order.setTotalDiscount(order.getTotalDiscount().add(BigDecimal.valueOf(orderProduct.getDiscount())));
            product.setQuantity(product.getQuantity() - Integer.parseInt(fieldQuantity.getText()));
            orderProductService.addOrderProductToList(orderProduct);
            orderProduct.setDescription(fieldQuantity.getText() + " " + product.getName() + ",");
            summary.fillDescriptionCalculateTotalPriceAndDiscount(orderId);
            labelDescription.setText(summary.getDescription());
            labelSum.setText(String.valueOf(summary.getSum()));
            labelDiscount.setText(String.valueOf(summary.getTotalDiscount()));
            loadTable();
            clearFields();
        } catch (NullPointerException e) {
            e.printStackTrace();
            labelAlert.setText("Please select product!");
        }
    }

    private void updateOrderProduct() {
        try {
            String productName = String.valueOf(comboBoxProducts.getValue());
            Product product = productService.getProductByName(productName);
            orderProduct.setOrderId(order.getTransactionID());
            orderProduct.setProductId(product.getId());
            orderProduct.setProductName(product.getName());
            orderProduct.setProductQuantity(Integer.parseInt(fieldQuantity.getText()));
            orderProduct.setProductPrice(product.getPrice());
            order.setTotalPrice(order.getTotalPrice().subtract(orderProduct.getTotalPrice()));
            order.setTotalDiscount(order.getTotalDiscount().subtract(BigDecimal.valueOf(orderProduct.getDiscount())));
            orderProduct.setTotalPrice(new BigDecimal(Double.toString(product.getPrice())).multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText())));
            order.setTotalPrice(order.getTotalPrice().add(orderProduct.getTotalPrice()));
            orderProduct.setDiscount(Float.parseFloat(fieldDiscount.getText()));
            order.setTotalDiscount(order.getTotalDiscount().add(BigDecimal.valueOf(orderProduct.getDiscount())));
            orderProduct.setDescription(fieldQuantity.getText() + " " + product.getName() + ",");
            summary.fillDescriptionCalculateTotalPriceAndDiscount(orderId);
            labelDescription.setText(summary.getDescription());
            labelDiscount.setText(String.valueOf(summary.getTotalDiscount()));
            labelSum.setText(String.valueOf(summary.getSum()));
            product.setQuantity(product.getQuantity() - Integer.parseInt(fieldQuantity.getText()));
            tableView.getSelectionModel().clearSelection();
            clearFields();
            loadTable();
            buttonAdd.setText("ADD");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void loadTable() {
        ObservableList list = orderProductService.getOrderProductByOrderId(orderId);
        tableView.setItems(list);
    }

    private void clearFields() {
        fieldPrice.setText("0");
        fieldTotalPrice.setText("0");
        fieldDiscount.setText("0");
        fieldQuantity.setText("0");
        labelPossibleQuantity.setText("0");
        buttonAdd.setText("ADD");
        comboBoxProducts.getSelectionModel().clearSelection();
    }

    public void getSelectedRow() {
        tableView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<OrderProduct>) (observableValue, oldValue, newValue) -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                OrderProduct selectedRow = (OrderProduct) tableView.getSelectionModel().getSelectedItem();
                updateTableRow(selectedRow);
            }
        });

    }

    private void setOrderProduct(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    private void updateTableRow(OrderProduct selectedRow) {
        fieldQuantity.setText(String.valueOf(selectedRow.getProductQuantity()));
        fieldPrice.setText(String.valueOf(selectedRow.getProductPrice()));
        fieldDiscount.setText(String.valueOf(selectedRow.getDiscount()));
        fieldTotalPrice.setText(String.valueOf(selectedRow.getTotalPrice()));
        comboBoxProducts.setValue(selectedRow.getProductName());
        buttonAdd.setText("Update");
        setOrderProduct(selectedRow);
    }

    private void selectFieldTextOnClick(TextField field) {
        field.focusedProperty().addListener((ov, t, t1) -> Platform.runLater(() -> {
            if (field.isFocused() && !field.getText().isEmpty()) {
                field.selectAll();
            }
        }));
    }

    private void disableSaveButtonFieldsEmpty() {
        BooleanBinding booleanBinding = new BooleanBinding() {
            {
                super.bind(fieldCustomerName.textProperty(), fieldCustomerAddress.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (fieldCustomerName.getText().isEmpty() || fieldCustomerAddress.getText().isEmpty());
            }
        };
        buttonSave.disableProperty().bind(booleanBinding);
    }

    private void fieldInputValidation() {
        selectFieldTextOnClick(fieldDiscount);
        selectFieldTextOnClick(fieldPrice);
        selectFieldTextOnClick(fieldTotalPrice);
        selectFieldTextOnClick(fieldQuantity);
        fieldQuantity.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (NumberUtils.isNumberInteger(newValue)) {
                fieldTotalPrice.setText(String.valueOf(new BigDecimal(Double.toString(product.getPrice())).multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()))));
                fieldQuantity.pseudoClassStateChanged(errorClass, false);
                labelAlert.setText("");
            } else {
                fieldQuantity.pseudoClassStateChanged(errorClass, true);
                labelAlert.setText(ALERT_TEXT);
            }
        }));

        fieldPrice.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$")) {
                fieldPrice.pseudoClassStateChanged(errorClass, true);
                labelAlert.setText(ALERT_TEXT);
            } else {
                fieldPrice.pseudoClassStateChanged(errorClass, false);
                labelAlert.setText("");
                fieldTotalPrice.setText(String.valueOf(new BigDecimal(Double.toString(product.getPrice())).multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()))));
            }
        }));

        fieldDiscount.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (NumberUtils.isNumberFloat(newValue)) {
                fieldDiscount.pseudoClassStateChanged(errorClass, false);
                labelAlert.setText("");
                fieldTotalPrice.setText(String.valueOf(new BigDecimal(Double.toString(product.getPrice())).multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()))));
            } else {
                fieldDiscount.pseudoClassStateChanged(errorClass, true);
                labelAlert.setText(ALERT_TEXT);
            }
        }));
    }

    private void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService = new ProductService();
        order = new Order();
        orderProductService = new OrderProductService();
        orderService = new OrderService();
        summary = new OrderProductSummary();
        populateTable();
        fieldInputValidation();
        comboOrderType.setItems(OrderType.getOrderTypeList());
        tableView.setItems(orderProductService.getOrderProductByOrderId(orderId));
        comboBoxProducts.setItems(productService.getProductNames());
        disableSaveButtonFieldsEmpty();
        order.setTransactionID(orderService.getOrderNewId());
        getSelectedRow();
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
        loadTable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

