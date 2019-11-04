package controller;

import dtos.OrderProductsDTO;
import enums.OrderType;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Order;
import model.OrderProduct;
import model.OrderProductSummary;
import model.Product;
import service.OrderProductService;
import service.OrderService;
import service.ProductService;
import service.serviceImpl.OrderProductServiceImpl;
import service.serviceImpl.OrderServiceImpl;
import service.serviceImpl.ProductServiceImpl;
import utils.NumberUtils;
import utils.TableCellStyleUtil;

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
    private OrderProductsDTO dto;
    private BigDecimal totalPrice;

    private static final Image imageDelete = new Image("/resources/images/trash_26px.png");
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
    private TextField fieldDescription;
    @FXML
    private Label labelSum;
    @FXML
    private Label labelDiscount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createInstance();
        fieldInputValidation();
        comboOrderType.setItems(OrderType.getOrderTypeList());
        loadComboBoxProducts();
        disableSaveButtonFieldsEmpty();
        getSelectedRow();
    }

    public void addButtonAction() {
        if (buttonAdd.getText().equals("ADD")) {
            addOrderProduct();
            loadTable();
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
        order.setCustomerName(fieldCustomerName.getText());
        order.setCustomerAddress(fieldCustomerAddress.getText());
        order.setOrderType(comboOrderType.getValue());
        orderService.updateOrderById(order,order.getId());
    }

    public void closeButtonAction() {
        Stage stage = (Stage) buttonClose.getScene().getWindow();
        stage.close();
    }

    public void buttonClearAction() {
        updateProduct(orderProduct);
        clearFields();
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
                        if (tableView.getSelectionModel() != null) {
                            updateProduct(orderProduct);
                        }
                        if (alert.getResult() == ButtonType.YES) {
                            OrderProduct orderProduct = (OrderProduct) getTableRow().getItem();
                            orderProductService.removeOrderProductById(orderProduct.getId(),orderProduct.getOrderId());
                            fillSummaryFields();
                            orderService.updateOrderById(order, order.getId());
                            loadTable();
                            clearFields();
                        }
                    });
                }
            }
        });
    }

    private void addOrderProduct() {
        try {
            createOrderProduct();
            if (validateOrderProduct(orderProduct)) {
                orderProductService.saveOrderProduct(orderProduct);
                fillSummaryFields();
                orderService.updateOrderById(order,order.getId());
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
                orderProductService.updateOrderProduct(orderProduct,orderProduct.getId());
                fillSummaryFields();
                orderService.updateOrderById(order,order.getId());
                clearFields();
                loadTable();
                buttonAdd.setText("ADD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillSummaryFields() {
        dto=orderProductService.getOrderProductByOrderId(order.getId());
        OrderProductSummary summary=dto.getSummary();
        if (summary!=null) {
            fieldDescription.setText(summary.getDescription());
            labelDiscount.setText(summary.getTotalDiscount().toString());
            labelSum.setText(summary.getTotalPrice().toString());
            order.setTotalPrice(summary.getTotalPrice());
            order.setTotalDiscount(summary.getTotalDiscount());
            order.setDescription(summary.getDescription());
        } else {
            fieldDescription.setText("");
            labelDiscount.setText("0");
            labelSum.setText("0");
            order.setTotalPrice(new BigDecimal("0"));
            order.setTotalDiscount(new BigDecimal("0"));
            order.setDescription("");
        }
    }

    private void loadTable() {
        dto = orderProductService.getOrderProductByOrderId(order.getId());
        if (dto.getOrderProducts()!=null) {
            tableView.setItems(FXCollections.observableArrayList(dto.getOrderProducts()));
        } else {
            tableView.setItems(FXCollections.observableArrayList());
        }
    }

    private void clearFields() {
        orderProduct=new OrderProduct();
        fieldPrice.setText("0");
        fieldTotalPrice.setText("0");
        fieldDiscount.setText("0");
        fieldQuantity.setText("0");
        labelPossibleQuantity.setText("0");
        buttonAdd.setText("ADD");
        comboBoxProducts.getSelectionModel().clearSelection();
        comboBoxProducts.disableProperty().setValue(false);
        loadComboBoxProducts();
        loadTable();
    }

    private void createOrderProduct() {
        product = comboBoxProducts.getValue();
        orderProduct = new OrderProduct();
        orderProduct.setOrderId(order.getId());
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
        tableView.setRowFactory(tv -> {
            TableRow<OrderProduct> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
                    updateProduct(orderProduct);
                    OrderProduct orderProduct = row.getItem();
                    setOrderProduct(orderProduct);
                    updateTableRow();
                }
            });
            return row ;
        });
    }

    private void setOrderProduct(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    private void updateTableRow() {
        product=productService.getProductById(orderProduct.getProductId());
        comboBoxProducts.setItems(FXCollections.observableArrayList(product));
        comboBoxProducts.getSelectionModel().selectFirst();
        fieldQuantity.setText(String.valueOf(orderProduct.getProductQuantity()));
        fieldPrice.setText(String.valueOf(orderProduct.getProductPrice()));
        fieldDiscount.setText(String.valueOf(orderProduct.getDiscount()));
        fieldTotalPrice.setText(orderProduct.getTotalPrice().toString());
        comboBoxProducts.disableProperty().setValue(true);
        product.setQuantity(orderProduct.getProductQuantity()+product.getQuantity());
        labelPossibleQuantity.setText(String.valueOf(product.getQuantity()));
        productService.updateProduct(product,product.getId());
        buttonAdd.setText("Update");
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
                    totalPrice = calculateTotalPrice(String.valueOf(product.getPrice()),fieldQuantity.getText(),fieldDiscount.getText());
                    totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    fieldTotalPrice.setText(totalPrice.toString());
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
                    totalPrice = calculateTotalPrice(String.valueOf(product.getPrice()),fieldQuantity.getText(),fieldDiscount.getText());
                    totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    fieldTotalPrice.setText(totalPrice.toString());
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
        productService = new ProductServiceImpl();
        order = new Order();
        orderProductService = new OrderProductServiceImpl();
        orderService = new OrderServiceImpl();
    }

    private boolean validateOrderProduct(OrderProduct orderProduct) {
        Map<String, Map<Boolean, List<String>>> validation = orderProductService.validateOrderProduct(orderProduct);
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
                errors.append(s + "\n");
            }
        }

        if (validation.get("discountError").containsKey(true)) {
            fieldDiscount.pseudoClassStateChanged(errorClass, true);
            Map<Boolean, List<String>> discountMap = validation.get("discountError");
            List<String> list = discountMap.get(true);
            for (String s : list) {
                errors.append(s + "\n");
            }
        }

        if (validation.get("totalPriceError").containsKey(true)) {
            fieldTotalPrice.pseudoClassStateChanged(errorClass, true);
            Map<Boolean, List<String>> totalPriceMap = validation.get("totalPriceError");
            List<String> list = totalPriceMap.get(true);
            for (String s : list) {
                errors.append(s + "\n");
            }
        }
        labelAlert.setText(String.valueOf(errors));
    }

    void setFields(Order order) {
        this.order = order;
        if (order != null) {
            fieldCustomerName.setText(order.getCustomerName());
            fieldCustomerAddress.setText(order.getCustomerAddress());
            comboOrderType.setValue(order.getOrderType());
            fieldDescription.setText(order.getDescription());
            labelSum.setText(String.valueOf(order.getTotalPrice()));
            labelDiscount.setText(String.valueOf(order.getTotalDiscount()));
        }
        populateTable();
        loadTable();
    }

    private void loadComboBoxProducts() {
        ObservableList<Product> list = null;
        list = productService.getProductListForComboBox();
        comboBoxProducts.getItems().clear();
        comboBoxProducts.setItems(list);
        addQuantityToComboBox();
    }

    private void addQuantityToComboBox() {
        Callback<ListView<Product>, ListCell<Product>> factory = lv -> new ListCell<Product>() {
            Label productLabel = new Label();
            Label detailLabel = new Label();

            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    detailLabel.setText(item.getQuantity() + "-" + item.getPrice());
                    productLabel.setText(item.getName());
                    AnchorPane pane = new AnchorPane(productLabel, detailLabel);
                    AnchorPane.setLeftAnchor(productLabel, 0.0);
                    AnchorPane.setRightAnchor(detailLabel, 0.0);
                    ;
                    setGraphic(pane);
                }
            }

        };
        comboBoxProducts.setCellFactory(factory);

        comboBoxProducts.setButtonCell(new ListCell<Product>(){
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void updateProduct(OrderProduct orderProduct) {
        if(orderProduct!=null) {
            Product product = productService.getProductById(orderProduct.getProductId());
            if (product != null) {
                product.setQuantity(product.getQuantity() - orderProduct.getProductQuantity());
                productService.updateProduct(product, orderProduct.getProductId());
            }
        }
    }

    private BigDecimal calculateTotalPrice(String price,String quantity,String discount) {
        return new BigDecimal(price).multiply(new BigDecimal(quantity)).subtract(new BigDecimal(discount));
    }
}

