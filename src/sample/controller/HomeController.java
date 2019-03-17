package sample.controller;

import javafx.event.ActionEvent;
import sample.utils.ScreenUtils;

import java.io.IOException;

public class HomeController {


    public void btnSalesAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event,"../screens/orderpage.fxml");
    }

    public void btnStockAction(ActionEvent event) throws IOException {
        ScreenUtils.changeScreen(event, "../screens/stockpage.fxml");
    }
}
