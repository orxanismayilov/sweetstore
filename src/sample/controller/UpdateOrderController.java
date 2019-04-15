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
import javafx.util.Callback;
import sample.enums.OrderType;
import sample.model.*;
import sample.service.OrderProductService;
import sample.service.OrderService;
import sample.service.ProductService;
import sample.utils.TableCellStyleUtil;
import sample.utils.NumberUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class UpdateOrderController implements Initializable {

    private OrderProduct orderProduct;
    private ProductService productService;
    private OrderProductService orderProductService;
    private Order order;
    private Product product;
    private OrderService orderService;
    private OrderProductSummary summary;
    private int orderId;
    private BigDecimal totalPrice;

    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static String ALERT_TEXT = "Please enter valid input!";
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");


    @FXML
    private ComboBox<Product> comboBoxProducts;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createInstance();
        populateTable();
        fieldInputValidation();
        comboOrderType.setItems(OrderType.getOrderTypeList());
        loadTable();
        loadComboBoxProducts();
        disableSaveButtonFieldsEmpty();
        getSelectedRow();
    }

    public void addButtonAction() throws Exception {
        if (buttonAdd.getText().equals("ADD")) {
            addOrderProduct();
        } else {
            updateButtonOrderProduct();
        }
    }

    public void comboBoxAction() {
        product = comboBoxProducts.getValue();
        if (product != null) {
            this.product = comboBoxProducts.getValue();
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
        order.setTotalDiscount(summary.getTotalDiscount());
        order.setTotalPrice(summary.getSum());
        order.setDescription(summary.getDescription());
        orderService.updateOrderById(order, orderId);
        closeButtonAction();
    }

    public void closeButtonAction() {
        Stage stage = (Stage) buttonClose.getScene().getWindow();
        stage.close();
    }

    void setOrderId(int orderId) {
        this.orderId = orderId;
        loadTable();
        setFields();
    }

    private void populateTable() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        columnPrice.setCellFactory(tc -> TableCellStyleUtil.setMonetaryColumnStyle());

        columnDiscount.setCellFactory(tc -> TableCellStyleUtil.setMonetaryColumnStyle());

        columnTotalPrice.setCellFactory(tc -> TableCellStyleUtil.setMonetaryColumnStyle());

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

    private void addOrderProduct() throws Exception {
        try {
            createOrderProduct();
            if (validateOrderProduct(orderProduct)) {
                fillSummaryFields();
                loadTable();
                clearFields();
            } else {
                orderProduct = new OrderProduct();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            labelAlert.setText("Please select product!");
        }
    }

    private void updateButtonOrderProduct() {
        try {
            updateOrderProduct();
            if (validateOrderProduct(orderProduct)) {
                fillSummaryFields();
                clearFields();
                loadTable();
                buttonAdd.setText("ADD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillSummaryFields() {
        summary.fillDescriptionCalculateTotalPriceAndDiscount(orderId);
        labelDescription.setText(String.valueOf(summary.getDescription()));
        labelDiscount.setText(String.valueOf(summary.getTotalDiscount()));
        labelSum.setText(String.valueOf(summary.getSum()));
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
        tableView.getSelectionModel().clearSelection();
    }

    private void createOrderProduct() {
        product = comboBoxProducts.getValue();
        orderProduct = new OrderProduct();
        orderProduct.setOrderId(order.getTransactionID());
        orderProduct.setProductId(product.getId());
        orderProduct.setProductName(product.getName());
        orderProduct.setProductQuantity(Integer.parseInt(fieldQuantity.getText()));
        orderProduct.setProductPrice(product.getPrice());
        orderProduct.setTotalPrice(totalPrice);
        orderProduct.setDiscount(Float.parseFloat(fieldDiscount.getText()));
        orderProduct.setDescription(fieldQuantity.getText() + " " + product.getName() + " ");
    }

    private void updateOrderProduct() {
        orderProduct.setProductQuantity(Integer.parseInt(fieldQuantity.getText()));
        orderProduct.setProductPrice(product.getPrice());
        orderProduct.setTotalPrice(totalPrice);
        orderProduct.setDiscount(Float.parseFloat(fieldDiscount.getText()));
        orderProduct.setDescription(fieldQuantity.getText() + " " + product.getName() + ",");
    }

    private void getSelectedRow() {
        tableView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<OrderProduct>) (observableValue, oldValue, newValue) -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                OrderProduct selectedRow = (OrderProduct) tableView.getSelectionModel().getSelectedItem();
                try {
                    updateTableRow(selectedRow);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setOrderProduct(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    private void updateTableRow(OrderProduct selectedRow) throws Exception {
        product=productService.getProductById(selectedRow.getProductId());
        fieldQuantity.setText(String.valueOf(selectedRow.getProductQuantity()));
        fieldPrice.setText(String.valueOf(selectedRow.getProductPrice()));
        fieldDiscount.setText(String.valueOf(selectedRow.getDiscount()));
        fieldTotalPrice.setText(String.valueOf(selectedRow.getTotalPrice()));
        comboBoxProducts.setValue(product);
        buttonAdd.setText("Update");
        loadComboBoxProducts();
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
                fieldQuantity.pseudoClassStateChanged(errorClass, false);
                labelAlert.setText("");
                if (comboBoxProducts.getValue() != null) {
                    totalPrice=new BigDecimal(Double.toString(product.getPrice())).multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()));
                    totalPrice=totalPrice.setScale(2,BigDecimal.ROUND_HALF_EVEN);
                    fieldTotalPrice.setText(String.valueOf(totalPrice));
                } else {
                    fieldTotalPrice.setText("0");
                }
            } else {
                fieldQuantity.pseudoClassStateChanged(errorClass, true);
                labelAlert.setText(ALERT_TEXT);
            }
        }));

        fieldDiscount.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (NumberUtils.isNumberFloat(newValue)) {
                fieldDiscount.pseudoClassStateChanged(errorClass, false);
                labelAlert.setText("");
                if (comboBoxProducts.getValue() != null) {
                    totalPrice=new BigDecimal(Double.toString(product.getPrice())).multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()));
                    totalPrice=totalPrice.setScale(2,BigDecimal.ROUND_HALF_EVEN);
                    fieldTotalPrice.setText(String.valueOf(totalPrice));
                } else {
                    fieldTotalPrice.setText("0");
                }
            } else {
                fieldDiscount.pseudoClassStateChanged(errorClass, true);
                labelAlert.setText(ALERT_TEXT);
            }
        }));
    }

    private void createInstance() {
        productService = new ProductService();
        order = new Order();
        orderProductService = new OrderProductService();
        orderService = new OrderService();
        summary = new OrderProductSummary();
    }

    private boolean validateOrderProduct(OrderProduct orderProduct) throws Exception {
        Map<String, Map<Boolean, List<String>>> validation = orderProductService.addOrderProductToList(orderProduct);
        if (!validation.get("quantityError").containsKey(true) && !validation.get("discountError").containsKey(true) && !validation.get("totalPriceError").containsKey(true)) {
            handleErrors(validation);
            return true;
        } else {
            handleErrors(validation);
            return false;
        }
    }

    private void handleErrors(Map<String, Map<Boolean, List<String>>> validation) {
        StringBuilder errors = new StringBuilder();
        if (validation.get("quantityError").containsKey(true)) {
            fieldQuantity.pseudoClassStateChanged(errorClass, true);
            Map<Boolean, List<String>> quantityMap = validation.get("quantityError");
            List<String> list = quantityMap.get(true);
            for (String s : list) {
                errors.append(s);
            }
        }

        if (validation.get("discountError").containsKey(true)) {
            fieldDiscount.pseudoClassStateChanged(errorClass, true);
            Map<Boolean, List<String>> discountMap = validation.get("discountError");
            List<String> list = discountMap.get(true);
            for (String s : list) {
                errors.append(s);
            }
        }

        if (validation.get("totalPriceError").containsKey(true)) {
            fieldTotalPrice.pseudoClassStateChanged(errorClass, true);
            Map<Boolean, List<String>> totalPriceMap = validation.get("totalPriceError");
            List<String> list = totalPriceMap.get(true);
            for (String s : list) {
                errors.append(s);
            }
        }
        labelAlert.setText(String.valueOf(errors));
    }

    private void setFields() {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            fieldCustomerName.setText(order.getCustomerName());
            fieldCustomerAddress.setText(order.getCustomerAddress());
            comboOrderType.setValue(order.getOrderType());
            labelDescription.setText(order.getDescription());
            labelSum.setText(String.valueOf(order.getTotalPrice()));
            labelDiscount.setText(String.valueOf(order.getTotalDiscount()));
        }
    }

    private void loadComboBoxProducts() {
        ObservableList<Product> list = productService.getProductList();
        comboBoxProducts.getItems().clear();
        comboBoxProducts.setItems(list);
        addQuantityToComboBox();
    }

    private void addQuantityToComboBox() {
        Callback<ListView<Product>, ListCell<Product>> factory = lv -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getName() + "  " + item.getQuantity());
                }
            }

        };
        comboBoxProducts.setCellFactory(factory);
        comboBoxProducts.setButtonCell(factory.call(null));
    }
}

