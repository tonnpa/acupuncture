package builddb;

/**
 * Created by Ani on 8/13/14.
 */
public class RomanNumeral {
    public static int romanToInteger(String val) {
        String aux = val.toUpperCase();
        int sum = 0, max = aux.length(), i = 0;
        while (i < max) {
            if ((i + 1) < max && valueOf(aux.charAt(i + 1)) > valueOf(aux.charAt(i))) {
                sum += valueOf(aux.charAt(i + 1)) - valueOf(aux.charAt(i));
                i += 2;
            } else {
                sum += valueOf(aux.charAt(i));
                i += 1;
            }
        }
        return sum;
    }

    public static int valueOf(Character c) {
        char aux = Character.toUpperCase(c);
        switch (aux) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }
}
