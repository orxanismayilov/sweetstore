package sample.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import sample.service.OrderService;
import sample.service.ProductService;

public class AddButtonTableUtils {

    private static final Image imageDelete = new Image("/sample/resource/images/trash_26px.png");
    private static final Image imageUpdate = new Image("/sample/resource/images/edit_property_26px.png");
    private static final Image imageInfo = new Image("/sample/resource/images/info_24px.png");
    private final static String FXML_URL_UPDATEORDER = "/sample/resource/screens/neworder.fxml";

    public static Callback<TableColumn<Object, Void>, TableCell<Object, Void>> addButtonToTable(TableColumn column, TableColumn columnID, Object service) {

        return   new Callback<TableColumn<Object, Void>, TableCell<Object, Void>>() {
            @Override
            public TableCell<Object, Void> call(final TableColumn<Object, Void> param) {
                final TableCell<Object, Void> cell = new TableCell<Object, Void>() {
                    final ImageView buttonDeleteGraphic = new ImageView();
                    final ImageView buttonUpdateGraphic = new ImageView();
                    final ImageView buttonInfoGraphic= new ImageView();
                    private final Button buttonDelete = new Button();
                    private final Button buttonUpdate=new Button();
                    private final Button buttonInfo=new Button();
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        buttonDeleteGraphic.setImage(imageDelete);
                        buttonDeleteGraphic.setFitWidth(12);
                        buttonDeleteGraphic.setFitHeight(12);
                        buttonUpdateGraphic.setImage(imageUpdate);
                        buttonUpdateGraphic.setFitWidth(12);
                        buttonUpdateGraphic.setFitHeight(12);
                        buttonInfoGraphic.setImage(imageInfo);
                        buttonInfoGraphic.setFitHeight(14);
                        buttonInfoGraphic.setFitWidth(14);
                        buttonUpdate.setGraphic(buttonUpdateGraphic);
                        buttonDelete.setGraphic(buttonDeleteGraphic);
                        buttonInfo.setGraphic(buttonInfoGraphic);
                        HBox pane=new HBox(buttonDelete,buttonUpdate,buttonInfo);
                        pane.setSpacing(10);
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                            buttonDelete.setOnAction((ActionEvent event) -> {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure ?", ButtonType.YES, ButtonType.CANCEL);
                                alert.showAndWait();
                                if (alert.getResult() == ButtonType.YES) {
                                    if (service instanceof OrderService){
                                        ((OrderService) service).deleteOrderByTransactionId((Integer) columnID.getCellData(getTableRow().getIndex()));
                                    } else if(service instanceof ProductService){
                                        ((ProductService) service).deleteProductbyID((Integer) columnID.getCellData(getTableRow().getIndex()));
                                    }
                                }

                            });

                            buttonUpdate.setOnAction((ActionEvent event)->{
                                try {
                                    ScreenUtils.newScreen(event,FXML_URL_UPDATEORDER);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }


                            });
                        }
                    }
                };
                return cell;
            }
        };
    }
}



