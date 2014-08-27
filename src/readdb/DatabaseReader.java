package readdb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by NguyenTA on 20/8/2014.
 */
public class DatabaseReader {

    private String userName;
    private String password;
    private String serverName;
    private int portNumber;
    private String dbName;

    private Connection connection;

    public DatabaseReader() {
        userName = "tonnpa";
        password = "tonnpa92";
        serverName = "localhost";
        portNumber = 3306;
        dbName = "châm_cứu";
    }

    public void pullContent() {
        List<int[]> aPointNumbers = new ArrayList<>();
        List<String> aPointNames = new ArrayList<>();
        Statement statement;
        String query = "select SỐ_KINH, SỐ_HUYỆT, TÊN_HUYỆT from HUYỆT";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            int index = 0;
            while (rs.next()) {
                int[] aPointNumber = {rs.getInt("SỐ_KINH"), rs.getInt("SỐ_HUYỆT")};
                String aPointName = rs.getString("TÊN_HUYỆT");
                aPointNumbers.add(index, aPointNumber);
                aPointNames.add(index, aPointName);
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        for (int i = 0; i < Math.min(10, aPointNumbers.size()); i++) {
            System.out.println(aPointNumbers.get(i) + aPointNames.get(i));
        }
    }

    public Connection getConnection() {
        connection = null;
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", userName);
        connectionProperties.put("password", password);

        try {
            connection = DriverManager.getConnection(
                    "jdbc:" + "mysql" + "://" + serverName +
                            ":" + portNumber + "/" + dbName,
                    connectionProperties);
            System.out.println("connected to builddb");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }


}
