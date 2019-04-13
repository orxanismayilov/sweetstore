package sample.utils;


import javafx.scene.control.TableCell;

import java.text.NumberFormat;

public class TableCellStyleUtil {

    private static String MANAT_SYMBOL="\u20BC";

    public static  <K, V> TableCell<K, V> setMonetaryColumnStyle(){
        return new TableCell<K,V>(){
            @Override
            protected void updateItem(V item, boolean empty) {
                NumberFormat numberFormat= NumberFormat.getInstance();
                super.updateItem(item,empty);
                if (empty){
                    setText(null);
                } else {
                    setText(String.valueOf(numberFormat.format(item))+MANAT_SYMBOL);
                }
            }
        };
    }
}
