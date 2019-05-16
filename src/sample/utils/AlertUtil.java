package sample.utils;

import javafx.scene.control.Alert;

public class AlertUtil {
    public static Alert permissionAlert() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION,"You don't have permission\n"+"for this action.");
        return alert;
    }
}