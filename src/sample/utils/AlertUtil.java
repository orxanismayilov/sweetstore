package sample.utils;

import javafx.scene.control.Alert;

public class AlertUtil {

    private static String ALERT_TEXT="You don't have permission\n"+"for this action.";

    public static Alert permissionAlert() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION,ALERT_TEXT);
        return alert;
    }
}