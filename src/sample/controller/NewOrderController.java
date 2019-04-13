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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.model.*;
import sample.service.OrderProductService;
import sample.service.OrderService;
import sample.service.ProductService;
import sample.utils.NumberUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class NewOrderController implements Initializable {
    private OrderProduct orderProduct;
    private ProductService productService;
    private OrderProductService orderProductService;
    private Order order;
    private Product product;
    private OrderService orderService;
    private int orderId;
    OrderProductSummary summary;


    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static String ALERT_TEXT = "Please enter valid input!";
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");

    @FXML
    private ComboBox comboBoxProducts;
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
    private TableColumn<OrderProduct, Double> columnPrice;
    @FXML
    private TableColumn<OrderProduct, BigDecimal> columnTotalPrice;
    @FXML
    private TableColumn<OrderProduct, Double> columnDiscount;
    @FXML
    private TableColumn<OrderProduct, Void> columnAction;
    @FXML
    private Label labelAlert;
    @FXML
    private Label labelPossibleQuantity;
    @FXML
    private Label labelSum;
    @FXML
    private Label labelDiscount;
    @FXML
    private ComboBox<OrderType> comboOrderType;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService = new ProductService();
        orderProductService = new OrderProductService();
        orderService = new OrderService();
        populateTable();
        fieldInputValidation();
        comboBoxProducts.setItems(productService.getProductNames());
        disableSaveButtonIfFieldsEmpty();

        summary = new OrderProductSummary();
        comboOrderType.setItems(OrderType.getOrderTypeList());
        order = new Order();
        orderId = orderService.getOrderNewId();
        order.setTransactionID(orderId);
        getSelectedRow();
    }

    private void populateTable() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        columnPrice.setCellFactory(tc -> new TableCell<OrderProduct, Double>() {
            private final Label labelSign = new Label();
            private final Label labelPrice = new Label();

            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                NumberFormat numberFormat = NumberFormat.getInstance();
                labelSign.setText("\u20BC");
                AnchorPane pane = new AnchorPane(labelPrice, labelSign);
                AnchorPane.setLeftAnchor(labelPrice, 0.0);
                AnchorPane.setRightAnchor(labelSign, 0.0);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                if (empty) {
                    setGraphic(null);
                } else {
                    labelPrice.setText(numberFormat.format(price));
                    setGraphic(pane);
                }
            }
        });
        columnTotalPrice.setCellFactory(tc -> new TableCell<OrderProduct, BigDecimal>() {
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
                            fieldDescription.setText(String.valueOf(summary.getDescription()));
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
            fieldTotalPrice.setText("0");
        }
    }

    public void saveButtonAction() {
        order.setCustomerName(fieldCustomerName.getText());
        order.setCustomerAddress(fieldCustomerAddress.getText());
        order.setDescription(summary.getDescription());
        order.setOrderType(String.valueOf(comboOrderType.getValue()));
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
            orderProduct.setDescription(fieldQuantity.getText() + " " + product.getName() + " ");
            summary.fillDescriptionCalculateTotalPriceAndDiscount(orderId);
            fieldDescription.setText(summary.getDescription());
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
            product.setQuantity(product.getQuantity() + orderProduct.getProductQuantity());
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
            fieldDescription.setText(String.valueOf(summary.getDescription()));
            labelDiscount.setText(String.valueOf(summary.getTotalDiscount()));
            labelSum.setText(String.valueOf(summary.getSum()));
            product.setQuantity(product.getQuantity() - Integer.parseInt(fieldQuantity.getText()));
            clearFields();
            loadTable();
            buttonAdd.setText("ADD");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void loadTable() {
        ObservableList list = orderProductService.getOrderProductByOrderId(order.getTransactionID());
        tableView.setItems(list);
    }

    public void clearFields() {
        fieldPrice.setText("0");
        fieldTotalPrice.setText("0");
        fieldDiscount.setText("0");
        fieldQuantity.setText("0");
        labelPossibleQuantity.setText("0");
        buttonAdd.setText("ADD");
        comboBoxProducts.getSelectionModel().clearSelection();
        comboBoxProducts.disableProperty().setValue(false);
        tableView.getSelectionModel().clearSelection();
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
        comboBoxProducts.disableProperty().setValue(true);
        labelPossibleQuantity.setText(String.valueOf(orderProduct.getProductQuantity() + product.getQuantity()));
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

    private void disableSaveButtonIfFieldsEmpty() {
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
                try {
                    if (comboBoxProducts.getValue() != null) {
                        BigDecimal sum = new BigDecimal(Double.toString(product.getPrice())).multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()));
                        sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
                        fieldTotalPrice.setText(String.valueOf(sum));
                    }
                } catch (NumberFormatException e) {
                    fieldTotalPrice.setText("0");
                }

                fieldQuantity.pseudoClassStateChanged(errorClass, false);
                labelAlert.setText("");

            } else {
                fieldQuantity.pseudoClassStateChanged(errorClass, true);
                labelAlert.setText(ALERT_TEXT);
            }
        }));

        fieldDiscount.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (NumberUtils.isNumberFloat(newValue)) {
                try {
                    if (comboBoxProducts.getValue() != null) {
                        BigDecimal sum = new BigDecimal(Double.toString(product.getPrice())).multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()));
                        sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
                        fieldTotalPrice.setText(String.valueOf(sum));
                    }
                } catch (NumberFormatException e) {
                    fieldTotalPrice.setText("0");
                }
                fieldDiscount.pseudoClassStateChanged(errorClass, false);
                labelAlert.setText("");
            } else {
                fieldDiscount.pseudoClassStateChanged(errorClass, true);
                labelAlert.setText(ALERT_TEXT);
            }
        }));
    }

    private void setProduct(Product product) {
        this.product = product;
    }

}
