package sample.controller;

import javafx.event.ActionEvent;
import sample.utils.ScreenUtils;

import java.io.IOException;

public class HomeController {

    private final static String FXML_URL_ORDERPAGE="../resource/screens/orderpage.fxml";
    private final static String FXML_URL_STOCKPAGE="../resource/screens/stockpage.fxml";

    public void btnSalesAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event,FXML_URL_ORDERPAGE);
    }

    public void btnStockAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event,FXML_URL_STOCKPAGE);
    }
}
