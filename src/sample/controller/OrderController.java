package sample.controller;

import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import sample.model.Order;
import sample.service.OrderService;
import sample.utils.ScreenUtils;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static sample.utils.AddButtonTableUtils.addButtonToTable;

public class OrderController implements Initializable {

    private OrderService orderService;
    private final static String FXML_URL_HOMEPAGE="../resource/screens/homepage.fxml";
    private final static String FXML_URL_UPDATEORDER= "/sample/resource/screens/neworder.fxml";
    private static final Image imageDelete=new Image("/sample/resource/images/trash_26px.png");
    private static final Image imageUpdate=new Image("/sample/resource/images/edit_property_26px.png");
    private static final Image imageInfo=new Image("/sample/resource/images/info_24px.png");
    @FXML private TableView<Order> tableView;
    @FXML private TableColumn<Order, String> clmName;
    @FXML private TableColumn<Order, String> clmAddress;
    @FXML private TableColumn<Order,Integer> clmTotalprice;
    @FXML private TableColumn<Order,String> clmOrdertype;
    @FXML private TableColumn<Order,Integer> clmTransactionID;
    @FXML private TableColumn<Order,String > clmDescription;
    @FXML private TableColumn<Order,LocalDate>clmDate;
    @FXML private TableColumn<Object, Void> clmAction;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderService=new OrderService();
        tableBinding();
        tableView.setItems(orderService.getData());
    }

    public void addOrder(ActionEvent event){
        Order order=new Order("das","Zaqatala","paxlava",1233,"online",1244845,LocalDate.of(2019,03,17));
        orderService.addData(order);
    }


    public void backButtonAction(ActionEvent event) throws Exception{
       ScreenUtils.changeScreen(event,FXML_URL_HOMEPAGE);
    }

    private void tableBinding(){
        clmName.setCellValueFactory(new PropertyValueFactory<Order,String>("customerName"));
        clmAddress.setCellValueFactory(new PropertyValueFactory<Order,String>("customerAddress"));
        clmDescription.setCellValueFactory(new PropertyValueFactory<Order,String >("description"));
        clmTotalprice.setCellValueFactory(new PropertyValueFactory<Order,Integer>("totalPrice"));
        clmOrdertype.setCellValueFactory(new PropertyValueFactory<Order, String>("orderType"));
        clmTransactionID.setCellValueFactory(new PropertyValueFactory<Order,Integer>("transactionID"));
        clmDate.setCellValueFactory(new PropertyValueFactory<Order,LocalDate>("date"));
        clmAction.setCellFactory(addButtonToTable(clmAction,clmTransactionID,orderService));
        clmAction.setResizable(false);
        clmAction.setMinWidth(120);
        clmAction.setMaxWidth(120);
    }

}
