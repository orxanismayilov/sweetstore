package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.model.Order;
import sample.service.OrderService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    private OrderService orderService;
    private ScreenController screenController;
    private Order order;
    @FXML private TableView<Order> tableView;
    @FXML private TableColumn<Order, String> clmName;
    @FXML private TableColumn<Order,Integer> clmTotalprice;
    @FXML private TableColumn<Order,String> clmOrdertype;
    @FXML private TableColumn<Order,Integer> clmTransactionID;
    @FXML private TableColumn<Order,String > clmDescription;
    @FXML private TableColumn<Order,LocalDate>clmDate;
    @FXML private TableColumn<Order,Void> clmAction;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderService=new OrderService();
        tableBinding();
        tableView.setItems(orderService.getData());
        addButtonToTable();
    }

    public void addOrder(ActionEvent event){
        Order order=new Order("das","paxlava",1233,"online",1244845,LocalDate.of(2019,03,17));
        orderService.addData(order);
        tableView.setItems(orderService.getData());
    }

    public void deleteData(ActionEvent event){
        orderService.deleteDataLast();
        tableView.setItems(orderService.getData());


    }

    public void backButtonAction(ActionEvent event) throws Exception{
       screenController=new ScreenController();
       screenController.changeScreen(event,"../screens/homepage.fxml");
    }

    private void tableBinding(){
        clmName.setCellValueFactory(new PropertyValueFactory<Order,String>("names"));
        clmDescription.setCellValueFactory(new PropertyValueFactory<Order,String >("description"));
        clmTotalprice.setCellValueFactory(new PropertyValueFactory<Order,Integer>("totalprice"));
        clmOrdertype.setCellValueFactory(new PropertyValueFactory<Order, String>("ordertype"));
        clmTransactionID.setCellValueFactory(new PropertyValueFactory<Order,Integer>("trasactionid"));
        clmDate.setCellValueFactory(new PropertyValueFactory<Order,LocalDate>("date"));
    }

    private void addButtonToTable() {
        Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<Order, Void>() {
                    final Image imageDelete=new Image("/sample/images/trash_26px.png");
                    final Image imageUpdate=new Image("/sample/images/edit_property_26px.png");
                    final ImageView buttonDeleteGraphic = new ImageView();
                    final ImageView buttonUpdateGraphic = new ImageView();
                    private final Button buttonDelete = new Button();
                    private final Button buttonUpdate=new Button();
                    {
                        buttonDelete.setOnAction((ActionEvent event) -> {
                            orderService.deleteDataLast();
                        });

                        buttonUpdate.setOnAction((ActionEvent event)->{
                          try {
                              Parent newPage=FXMLLoader.load(getClass().getResource("/sample/screens/neworder.fxml"));
                              Stage stage=new Stage();
                              stage.setScene(new Scene(newPage,700,400));
                              stage.setTitle("Update");
                              stage.show();
                          }catch (Exception e){
                              e.printStackTrace();
                          }


                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        buttonDeleteGraphic.setImage(imageDelete);
                        buttonDeleteGraphic.setFitWidth(20);
                        buttonDeleteGraphic.setFitHeight(20);
                        buttonUpdateGraphic.setImage(imageUpdate);
                        buttonUpdateGraphic.setFitWidth(20);
                        buttonUpdateGraphic.setFitHeight(20);
                        buttonUpdate.setGraphic(buttonUpdateGraphic);
                        buttonDelete.setGraphic(buttonDeleteGraphic);
                        HBox pane=new HBox(buttonDelete,buttonUpdate);
                        pane.setSpacing(10);
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
                return cell;
            }
        };

        clmAction.setCellFactory(cellFactory);
        clmAction.setMaxWidth(130);
        clmAction.setMinWidth(130);


    }

}
