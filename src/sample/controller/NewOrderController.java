package sample.controller;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import sample.model.Order;
import sample.model.OrderProduct;
import sample.model.Product;
import sample.service.OrderProductService;
import sample.service.OrderService;
import sample.service.ProductService;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
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
    private ObservableList<String> comboList;

    private BigDecimal sum;
    private BigDecimal totalDiscount;
    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static String ALERT_TEXT="Please enter valid input!";
    private StringBuilder DESCRIPTION_TEXT;
    private final static PseudoClass errorClass = PseudoClass.getPseudoClass("filled");

    @FXML private ComboBox comboBoxProducts;
    @FXML private ComboBox comboOrderType;
    @FXML private Button buttonSave;
    @FXML private Button buttonAdd;
    @FXML private TextField fieldCustomerName;
    @FXML private TextField fieldCustomerAddress;
    @FXML private TextField fieldQuantity;
    @FXML private TextField fieldPrice;
    @FXML private TextField fieldTotalPrice;
    @FXML private TextField fieldDiscount;
    @FXML private TableView tableView;
    @FXML private TableColumn<OrderProduct,Integer> columnOrderId;
    @FXML private TableColumn<OrderProduct,String>  columnProduct;
    @FXML private TableColumn<OrderProduct,Integer> columnQuantity;
    @FXML private TableColumn<OrderProduct,BigDecimal> columnPrice;
    @FXML private  TableColumn<OrderProduct,BigDecimal> columnTotalPrice;
    @FXML private TableColumn<OrderProduct,Double> columnDiscount;
    @FXML private TableColumn<OrderProduct,Void> columnAction;
    @FXML private Label labelAlert;
    @FXML private Label labelPossibleQuantity;
    @FXML private Label labelDescription;
    @FXML private Label labelSum;
    @FXML private Label labelDiscount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService=new ProductService();
        order=new Order();
        sum=new BigDecimal(0);
        totalDiscount=new BigDecimal(0);
        orderProductService=new OrderProductService();
        orderService=new OrderService();
        populateTable();
       // totalPriceAutoFill();
        fieldInputValidation();
        tableView.setItems(orderProductService.getOrderProductList());
        comboBoxProducts.setItems(fillComboList());
        manageFocus();
        order.setTransactionID(orderService.getOrderNewId());
        getSelectedRow();
    }

    public ObservableList fillComboList(){
        comboList= FXCollections.observableArrayList();
        ObservableList<Product> list=productService.getData();
        for(Product product:list){
            comboList.add(product.getName());
        }
        return comboList;
    }

    private void populateTable(){
        columnOrderId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        columnPrice.setCellFactory(tc -> new TableCell<OrderProduct, BigDecimal>() {
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
        columnAction.setCellFactory(tc->new TableCell<OrderProduct,Void>(){
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
                    });
                }
            }
        });
    }

    public void addButtonAction(){
        if (buttonAdd.getText().equals("ADD")) {
            addOrderProduct();
        } else {
            updateOrderProduct();
        }
    }

    public void comboBoxAction(){
        Product product=productService.getProductByName(String.valueOf(comboBoxProducts.getValue()));
        setProduct(product);
        fieldPrice.setText(String.valueOf(product.getPrice()));
        labelPossibleQuantity.setText(String.valueOf(product.getQuantity()));

    }

    public void saveButtonAction() {
        fillDescription();
        order.setCustomerName(fieldCustomerName.getText());
        order.setCustomerAddress(fieldCustomerAddress.getText());
        order.setDescription(DESCRIPTION_TEXT);
        order.setOrderType("online");
        order.setTotalPrice(sum);
        orderService.addData(order);
    }

    private void addOrderProduct(){
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
            orderProduct.setTotalPrice(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText())));
            orderProduct.setDiscount(Double.parseDouble(fieldDiscount.getText()));
            product.setQuantity(product.getQuantity() - Integer.parseInt(fieldQuantity.getText()));
            orderProductService.addOrderProducttoList(orderProduct);
            orderProduct.setDescription(fieldQuantity.getText() + " "+product.getName() + ",");
            fillDescription();
            labelDescription.setText(String.valueOf(DESCRIPTION_TEXT));
            sum = sum.add(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText())));
            totalDiscount = totalDiscount.add(new BigDecimal(fieldDiscount.getText()));
            labelSum.setText(String.valueOf(sum));
            labelDiscount.setText(String.valueOf(totalDiscount));
            loadTable();
            clearFields();
        } catch (NullPointerException e) {
            e.printStackTrace();
            labelAlert.setText("Please select product!");
            return;
        }
    }

    private void updateOrderProduct(){
        String productName = String.valueOf(comboBoxProducts.getValue());
        Product product = productService.getProductByName(productName);
        orderProduct.setOrderId(order.getTransactionID());
        orderProduct.setProductId(product.getId());
        orderProduct.setProductName(product.getName());
        orderProduct.setProductQuantity(Integer.parseInt(fieldQuantity.getText()));
        orderProduct.setProductPrice(product.getPrice());
        orderProduct.setTotalPrice(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText())));
        orderProduct.setDiscount(Double.parseDouble(fieldDiscount.getText()));
        orderProduct.setDescription(fieldQuantity.getText() + " "+product.getName() + ",");
        fillDescription();
        labelDescription.setText(String.valueOf(DESCRIPTION_TEXT));
        product.setQuantity(product.getQuantity() - Integer.parseInt(fieldQuantity.getText()));
        tableView.getSelectionModel().clearSelection();
        clearFields();
        loadTable();
        buttonAdd.setText("ADD");
    }

    private void fillDescription(){
       ObservableList<OrderProduct> list=orderProductService.getOrderProductList();
       DESCRIPTION_TEXT=new StringBuilder();
       for(OrderProduct orderProduct:list){
           DESCRIPTION_TEXT.append(orderProduct.getDescription());
       }
    }

    private void loadTable(){
        ObservableList list=orderProductService.getOrderProductList();
        tableView.setItems(list);
    }

    private void clearFields(){
        fieldPrice.setText("0");
        fieldTotalPrice.setText("0");
        fieldDiscount.setText("0");
        fieldQuantity.setText("0");
        labelPossibleQuantity.setText("0");
        buttonAdd.setText("ADD");
    }

    public void getSelectedRow(){
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderProduct>() {
            @Override
            public void changed(ObservableValue<? extends OrderProduct> observableValue, OrderProduct oldValue, OrderProduct newValue) {
                if (tableView.getSelectionModel().getSelectedItem() != null) {
                    OrderProduct selectedRow = (OrderProduct) tableView.getSelectionModel().getSelectedItem();
                    updateTableRow(selectedRow);
                }
            }
        });

    }

    private void setOrderProduct(OrderProduct orderProduct){
        this.orderProduct=orderProduct;
    }

    private void updateTableRow(OrderProduct selectedRow){
        fieldQuantity.setText(String.valueOf(selectedRow.getProductQuantity()));
        fieldPrice.setText(String.valueOf(selectedRow.getProductPrice()));
        fieldDiscount.setText(String.valueOf(selectedRow.getDiscount()));
        fieldTotalPrice.setText(String.valueOf(selectedRow.getTotalPrice()));
        comboBoxProducts.setValue(selectedRow.getProductName());
        buttonAdd.setText("Update");
        setOrderProduct(selectedRow);
    }



    private void selectFieldText(TextField field){
        field.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov, Boolean t, Boolean t1) {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (field.isFocused() && !field.getText().isEmpty()) {
                            field.selectAll();
                        }
                    }
                });
            }
        });
    }
    private void manageFocus(){
        BooleanBinding booleanBinding = new BooleanBinding() {
            {
                super.bind(fieldCustomerName.textProperty(),fieldCustomerAddress.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (fieldCustomerName.getText().isEmpty() || fieldCustomerAddress.getText().isEmpty());
            }
        };
        buttonSave.disableProperty().bind(booleanBinding);
    }

    private void fieldInputValidation(){
        selectFieldText(fieldDiscount);
        selectFieldText(fieldPrice);
        selectFieldText(fieldTotalPrice);
        selectFieldText(fieldQuantity);
        fieldQuantity.textProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$")) {
                        fieldQuantity.pseudoClassStateChanged(errorClass,true);
                        labelAlert.setText(ALERT_TEXT);
                } else {
                    fieldTotalPrice.setText(String.valueOf(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()))));
                    fieldQuantity.pseudoClassStateChanged(errorClass,false);
                    labelAlert.setText("");
                }
            }
        }));

        fieldPrice.textProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$")) {
                    fieldPrice.pseudoClassStateChanged(errorClass,true);
                    labelAlert.setText(ALERT_TEXT);
                } else {
                    fieldPrice.pseudoClassStateChanged(errorClass,false);
                    labelAlert.setText("");
                    fieldTotalPrice.setText(String.valueOf(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()))));
                }
            }
        }));

        fieldDiscount.textProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$")) {
                    fieldDiscount.pseudoClassStateChanged(errorClass,true);
                    labelAlert.setText(ALERT_TEXT);
                } else {
                    fieldDiscount.pseudoClassStateChanged(errorClass,false);
                    labelAlert.setText("");
                    fieldTotalPrice.setText(String.valueOf(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()))));
                }
            }
        }));
    }

    private void setProduct(Product product) {
        this.product = product;
    }
}
