package SecondProcess.DAO;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;


public class DAO {

    private String tabName;
    private Connection connection = getMysqlConnection();
    private Executor executor;


    private Connection getMysqlConnection() {
        try {

            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            StringBuilder url = new StringBuilder();
            url.
                    append("jdbc:mysql://").
                    append("localhost:").
                    append("3306/").
                    append("YoBITMIREA?").
                    append("useSSL=false&").
                    append("user=root&").
                    append("password=root");

            return DriverManager.getConnection(url.toString());
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException  e) {
            e.printStackTrace();
        }
        return null;
    }
    void batchSQL(int numberOfVal, ArrayList<String> dataSql) throws SQLException {
        try {
            this.executor = new Executor(connection);
            String statement;
            switch (tabName){
                case "main_table":{
                    statement = "insert into main_table (valuta_name, Last, High, Low, V) values (?, ?, ?, ?, ?)";
                    break;
                }
                case "Chat": {
                    statement = "insert into chat (full_text, MAIN_TABLE_id) values (?, ?)";
                    break;
                }
                case "story": {
                    statement = "insert into story (Type, Time, Price, ETH, MAIN_TABLE_id) values (?, ?, ?, ?, ?)";
                    break;
                }
                default:{
                    statement = "insert into " + tabName + " (Price, ETH, BTC, MAIN_TABLE_id) values (?, ?, ?, ?)";
                    break;
                }
            }
            executor.setBatchProp(new BatchProperties(tabName, statement, 20, numberOfVal+1));
            if(tabName.equals("main_table")){
                executor.executeMain(dataSql);
            }else{
            for (String s : dataSql) {
                try {
                    executor.batchStrings(s);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            }

        }finally {
            executor.close();
        }

    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

}
