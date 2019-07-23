package controller;

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
import sample.enums.OrderType;
import sample.model.Order;
import sample.model.OrderProduct;
import sample.model.OrderProductSummary;
import sample.model.Product;
import sample.repository.impl.OrderDaoImpl;
import sample.repository.impl.OrderProductImpl;
import sample.repository.impl.ProductDaoImpl;
import sample.service.OrderProductService;
import sample.service.ProductService;
import sample.service.serviceImpl.OrderProductServiceImpl;
import sample.service.serviceImpl.OrderServiceImpl;
import sample.service.serviceImpl.ProductServiceImpl;
import sample.utils.NumberUtils;
import sample.utils.TableCellStyleUtil;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class NewOrderController implements Initializable {
    private OrderProduct orderProduct;
    private ProductService productService;
    private OrderProductService orderProductService;
    private Order order;
    private Product product;
    private OrderServiceImpl orderServiceImpl;
    private int orderId;
    private OrderProductSummary summary;


    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static String ALERT_TEXT = "Please enter valid input!";
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");

    @FXML
    private ComboBox<Product> comboBoxProducts;
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
    private Label labelSum;
    @FXML
    private Label labelDiscount;
    @FXML
    private ComboBox<OrderType> comboOrderType;
    @FXML
    private AnchorPane paneOrderDetails;
    @FXML
    private AnchorPane paneCustomerDetails;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService = new ProductServiceImpl(new ProductDaoImpl());
        orderProductService = new OrderProductServiceImpl(new OrderProductImpl());
        orderServiceImpl = new OrderServiceImpl(new OrderDaoImpl());
        populateTable();
        paneOrderDetails.setDisable(true);
        fieldInputValidation();
        loadComboBoxProducts();
        disableSaveButtonIfFieldsEmpty();
        summary = new OrderProductSummary();
        comboOrderType.setItems(OrderType.getOrderTypeList());
        order = new Order();
        getSelectedRow();
    }

    public void addButtonAction() {
        if (buttonAdd.getText().equals("ADD")) {
            addOrderProduct();
            orderServiceImpl.updateOrderById(order,orderId);
        } else {
            updateOrderProduct();
        }
    }

    public void comboBoxAction() {
        product = comboBoxProducts.getValue();
        if (product != null) {
            fieldPrice.setText(String.valueOf(product.getPrice()));
            labelPossibleQuantity.setText("/"+String.valueOf(product.getQuantity()));
            fieldTotalPrice.setText("0");

        }
    }

    public void saveButtonAction() {
        order.setCustomerName(fieldCustomerName.getText());
        order.setCustomerAddress(fieldCustomerAddress.getText());
        order.setOrderType(comboOrderType.getValue());
        orderId= orderServiceImpl.addNewOrderToList(order);
        paneCustomerDetails.setDisable(true);
        paneOrderDetails.setDisable(false);
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

        columnDiscount.setCellFactory(tc -> TableCellStyleUtil.setMonetaryColumnStyle());

        columnPrice.setCellFactory(tc -> TableCellStyleUtil.setMonetaryColumnStyle());

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
                            orderProductService.removeOrderProductById(orderProduct.getId());
                            fillSummaryFields();
                            order.setTotalPrice(summary.getSum());
                            order.setTotalDiscount(summary.getTotalDiscount());
                            order.setDescription(summary.getDescription());
                            orderServiceImpl.updateOrderById(order, order.getTransactionID());
                            loadTable();
                            clearFields();
                        }
                    });
                }
            }
        });
    }

    private void loadComboBoxProducts(){
        ObservableList<Product> list= null;
        list = productService.getProductListForComboBox();
        comboBoxProducts.getItems().clear();
        comboBoxProducts.setItems(list);
        addQuantityToComboBox();
    }

    private void addOrderProduct() {
        try {
            createOrderProduct();
            if (validateOrderProduct(orderProduct)) {
                orderProductService.saveOrderProduct(orderProduct);
                fillSummaryFields();
                order.setTotalDiscount(summary.getTotalDiscount());
                order.setTotalPrice(summary.getSum());
                order.setDescription(summary.getDescription());
                loadTable();
                loadComboBoxProducts();
                clearFields();
            } else {
                orderProduct = new OrderProduct();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            labelAlert.setText("Please select product!");
        }
    }

    private void updateOrderProduct() {
        try {
            orderProduct.setProductQuantity(Integer.parseInt(fieldQuantity.getText()));
            orderProduct.setTotalPrice(calculateTotalPrice(String.valueOf(product.getPrice()),fieldQuantity.getText(),fieldDiscount.getText()));
            orderProduct.setDiscount(Float.parseFloat(fieldDiscount.getText()));
            orderProduct.setDescription(fieldQuantity.getText() + " " + product.getName() + ",");
            if (validateOrderProduct(orderProduct)) {
                orderProductService.updateOrderProduct(orderProduct,orderProduct.getId());
                fillSummaryFields();
                clearFields();
                loadTable();
                loadComboBoxProducts();
                buttonAdd.setText("ADD");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createOrderProduct(){
        orderProduct = new OrderProduct();
        orderProduct.setOrderId(orderId);
        orderProduct.setProductId(product.getId());
        orderProduct.setProductName(product.getName());
        orderProduct.setProductQuantity(Integer.parseInt(fieldQuantity.getText()));
        orderProduct.setProductPrice(product.getPrice());
        orderProduct.setTotalPrice(calculateTotalPrice(String.valueOf(product.getPrice()),fieldQuantity.getText(),fieldDiscount.getText()));
        orderProduct.setDiscount(Float.parseFloat(fieldDiscount.getText()));
        orderProduct.setDescription(fieldQuantity.getText() + " " + product.getName() + " ");
        orderProduct.setActive(true);
    }

    private void fillSummaryFields(){
        summary.fillDescriptionCalculateTotalPriceAndDiscount(orderId);
        fieldDescription.setText(String.valueOf(summary.getDescription()));
        labelDiscount.setText(String.valueOf(summary.getTotalDiscount()));
        labelSum.setText(String.valueOf(summary.getSum()));
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
        } else {
            fieldQuantity.pseudoClassStateChanged(errorClass, false);
        }

        if (validation.get("discountError").containsKey(true)) {
            fieldDiscount.pseudoClassStateChanged(errorClass, true);
            Map<Boolean, List<String>> discountMap = validation.get("discountError");
            List<String> list = discountMap.get(true);
            for (String s : list) {
                errors.append(s + "\n");
            }
        } else {
            fieldDiscount.pseudoClassStateChanged(errorClass, false);
        }

        if (validation.get("totalPriceError").containsKey(true)) {
            fieldTotalPrice.pseudoClassStateChanged(errorClass, true);
            Map<Boolean, List<String>> totalPriceMap = validation.get("totalPriceError");
            List<String> list = totalPriceMap.get(true);
            for (String s : list) {
                errors.append(s + "\n");
            }
        } else {
            fieldTotalPrice.pseudoClassStateChanged(errorClass, false);
        }

        labelAlert.setText(String.valueOf(errors));
    }

    private void loadTable() {
        ObservableList list = orderProductService.getOrderProductByOrderId(orderId);
        tableView.setItems(list);
    }

    private void clearFields() {
        orderProduct= new OrderProduct();
        fieldPrice.setText("0");
        fieldTotalPrice.setText("0");
        fieldDiscount.setText("0");
        fieldQuantity.setText("0");
        labelPossibleQuantity.setText("/0");
        buttonAdd.setText("ADD");
        loadComboBoxProducts();
        comboBoxProducts.disableProperty().setValue(false);
        tableView.getSelectionModel().clearSelection();
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
        this.orderProduct=orderProduct;
    }

    private void addQuantityToComboBox(){
        Callback<ListView<Product>, ListCell<Product>> factory = lv -> new ListCell<Product>() {
            Label productLabel= new Label();
            Label detailLabel=new Label();
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);

                if (empty){
                    setGraphic(null);
                } else {
                    detailLabel.setText(item.getQuantity()+"-"+item.getPrice());
                    productLabel.setText(item.getName());
                    AnchorPane pane=new AnchorPane(productLabel,detailLabel);
                    AnchorPane.setLeftAnchor(productLabel, 0.0);
                    AnchorPane.setRightAnchor(detailLabel, 0.0);
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
        labelPossibleQuantity.setText("/"+String.valueOf(product.getQuantity()));
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

    private void disableSaveButtonIfFieldsEmpty() {
        BooleanBinding booleanBinding = new BooleanBinding() {
            {
                super.bind(fieldCustomerName.textProperty(), fieldCustomerAddress.textProperty() , comboOrderType.itemsProperty());
            }
            @Override
            protected boolean computeValue() {
                return (fieldCustomerName.getText().isEmpty() || fieldCustomerAddress.getText().isEmpty() || comboOrderType.getItems().isEmpty());
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
                        fieldTotalPrice.setText(String.valueOf(calculateTotalPrice(String.valueOf(product.getPrice()),fieldQuantity.getText(),fieldDiscount.getText())));

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
                        fieldTotalPrice.setText(String.valueOf(calculateTotalPrice(String.valueOf(product.getPrice()),fieldQuantity.getText(),fieldDiscount.getText())));
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

    private BigDecimal calculateTotalPrice(String price,String quantity,String discount) {
       return new BigDecimal(price).multiply(new BigDecimal(quantity)).subtract(new BigDecimal(discount));
    }
}
