/**
 * Created by Ani on 8/12/14.
 */
public class SQLCommand {

    public String insert_into(String table_name, String value1, String value2){
        return "insert into " + table_name + " values(" +
                "\'" + value1 + "\', " +
                "\'" + value2 + "\'" +
                ");";
    }

}
