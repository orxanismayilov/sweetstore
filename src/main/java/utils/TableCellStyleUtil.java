package utils;


import javafx.scene.control.TableCell;

import java.text.DecimalFormat;

public class TableCellStyleUtil {

    private static String MANAT_SYMBOL="\u20BC";
    private static String NUM_FORMAT_PATTERN="#,###,###,##0.00";

    public static  <K, V> TableCell<K, V> setMonetaryColumnStyle(){
        return new TableCell<K,V>(){
            @Override
            protected void updateItem(V item, boolean empty) {
                DecimalFormat numberFormat = new DecimalFormat(NUM_FORMAT_PATTERN);
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
