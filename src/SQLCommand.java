/**
 * Created by Ani on 8/12/14.
 */
public class SQLCommand {

    public static String insert_into(String table_name, String value1, String value2){
        return "insert into " + table_name + " values(" +
                "\'" + value1 + "\', " +
                "\'" + value2 + "\'" +
                ");";
    }

    public static String insert_into(String table_name, String value1, int value2, int value3, String value4){
        return ("insert into " + table_name + " values(" +
                "\'" + value1 + "\', " +
                value2 + ", " +
                value3 + ", " +
                "\'" + value4 + "\'" +
                ");").replaceAll("-1","NULL");
    }

}
