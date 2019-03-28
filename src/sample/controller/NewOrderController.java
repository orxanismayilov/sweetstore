package sample.controller;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import sample.service.ProductService;

import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class NewOrderController implements Initializable {
    private ProductService productService;
    private OrderProductService orderProductService;
    private Order order;
    private ObservableList<String> comboList;

    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");

    @FXML private ComboBox comboBoxProducts;
    @FXML private ComboBox comboOrderType;
    @FXML private Button buttonSave;
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
    @FXML private TableColumn<OrderProduct,Void> columnAction;
    @FXML private Label labelAlert;
    @FXML private Label labelPossibleQuantity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productService=new ProductService();
        order=new Order();
        orderProductService=new OrderProductService();
        populateTable();
        totalPriceAutoFill();
        tableView.setItems(orderProductService.getOrderProductList());
        comboBoxProducts.setItems(fillComboList());
        manageFocus();
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
       try {
           String productName= String.valueOf(comboBoxProducts.getValue());
           Product product=productService.getProductByName(productName);
           OrderProduct orderProduct=new OrderProduct();
           orderProduct.setOrderId(12);
           orderProduct.setProductId(product.getId());
           orderProduct.setProductName(product.getName());
           orderProduct.setProductQuantity(Integer.parseInt(fieldQuantity.getText()));
           orderProduct.setProductPrice(product.getPrice());
           orderProduct.setTotalPrice(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText())));
           orderProductService.addOrderProducttoList(orderProduct);
           loadTable();
           clearFields();
       }catch (NullPointerException e){
           e.printStackTrace();
           labelAlert.setText("Please select product!");
           return;
       }

    }

    public void comboBoxAction(){
        Product product=productService.getProductByName(String.valueOf(comboBoxProducts.getValue()));
        fieldPrice.setText(String.valueOf(product.getPrice()));
        labelPossibleQuantity.setText("/"+product.getQuantity());

    }

    private void loadTable(){
        ObservableList list=orderProductService.getOrderProductList();
        tableView.setItems(list);
    }

    private void clearFields(){
        comboBoxProducts.setValue(null);
        fieldPrice.setPromptText("0");
        fieldTotalPrice.setPromptText("0");
        fieldDiscount.setPromptText("0");
        fieldQuantity.setPromptText("0");

    }

    private void totalPriceAutoFill(){
        selectFieldText(fieldDiscount);
        selectFieldText(fieldPrice);
        selectFieldText(fieldTotalPrice);
        selectFieldText(fieldQuantity);

        fieldQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String productName= String.valueOf(comboBoxProducts.getValue());
                Product product=productService.getProductByName(productName);
                fieldTotalPrice.setText(String.valueOf(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()))));
            }
        });

        fieldPrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String productName= String.valueOf(comboBoxProducts.getValue());
                Product product=productService.getProductByName(productName);
                fieldTotalPrice.setText(String.valueOf(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()))));
            }
        });

        fieldDiscount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String productName= String.valueOf(comboBoxProducts.getValue());
                Product product=productService.getProductByName(productName);
                fieldTotalPrice.setText(String.valueOf(product.getPrice().multiply(new BigDecimal(fieldQuantity.getText())).subtract(new BigDecimal(fieldDiscount.getText()))));
            }
        });
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


}
