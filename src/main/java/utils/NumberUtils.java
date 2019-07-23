package utils;

public class NumberUtils {

    private final static String FLOAT_REGEX = "^([0-9]+\\.?[0-9]*|[0-9]*\\.[0-9]+)$";
    private final static String INTEGER_REGEX = "\\d*";

    public static boolean isNumberInteger(String number) {
        if (number.matches(INTEGER_REGEX)) {
            return true;
        }
        return false;
    }

    public static boolean isNumberFloat(String number){
        if (number.matches(FLOAT_REGEX)){
            return true;
        }
        return false;
    }
}
