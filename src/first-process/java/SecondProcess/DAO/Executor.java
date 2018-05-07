package SecondProcess.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

class Executor {
    private final Connection connection;
    private PreparedStatement prstmt;
    private BatchProperties properties;
    private int numOfSt = 0;

    Executor(Connection connection) {
        this.connection = connection;
    }

    void setBatchProp(BatchProperties properties) throws SQLException {
        prstmt = connection.prepareStatement(properties.getPreparedValue());
        this.properties = properties;
    }

    public void executeInsert(String update) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(update);
        stmt.close();
    }

    public <T> T executeQuery(String query,
                           ResultHandler<T> handler)
            throws SQLException {

        T value;
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        try {
            value = handler.handle(result);
            return value;
        }catch (SQLException sqlE){
            return null;
            //если значение не найдено - вывести null
        }finally {
            System.out.println();
            result.close();
            stmt.close();
        }


    }

    void batchStrings(String list) throws SQLException{
        {
            String[] array = list.split(" ");
            switch (properties.getTableName()){
                case "Chat": {
                    prstmt.setInt(2, properties.getNumVal());
                    prstmt.setString(1, list);
                    break;
                }
                case "story": {
                    for (int i = 0; i<array.length; i++) {
                        if (i == 1) {
                            prstmt.setString(1, String.valueOf(array[i].charAt(0)));
                        }
                        else if (i == 0 ) {
                            prstmt.setTime(2, convertDate(array[i]));
                        }
                        else{
                            prstmt.setDouble(i+1, Double.parseDouble(array[i]));
                        }
                    }
                    prstmt.setInt(5, properties.getNumVal());
                    break;
                }
                default: {
                    for (int i = 0; i<array.length; i++) {
                        prstmt.setDouble(i + 1, Double.parseDouble(array[i]));

                    }
                    prstmt.setInt(4, properties.getNumVal());
                   break;
                }
        }
        }
        prstmt.addBatch();
        numOfSt++;
            //отправка если >20 записей
        if (numOfSt > properties.getBatchMax()) {
            prstmt.executeBatch();
            numOfSt = 0;
        }

    }
    void executeMain(ArrayList<String> list) throws SQLException {
        for (int i = 0; i<list.size(); i++) {
            if(i == 0) {
                prstmt.setString(i + 1, list.get(i));
            }else if (i == 4){
                prstmt.setString(i + 1, list.get(i).split(":")[1]);
            }else{
                prstmt.setDouble(i+1, Double.parseDouble(
                        list.get(i).split(" ")[1]));
            }
        }
        prstmt.addBatch();
        prstmt.executeBatch();
    }
    private java.sql.Time convertDate (String line){
        String mass[] = line.split(":");
        int Hour = Integer.parseInt((mass[0]));
        int Minutes = Integer.parseInt(mass[1]);
        int Seconds = Integer.parseInt(mass[2]);
        java.sql.Time time = new java.sql.Time(Hour, Minutes, Seconds);
        return time;
    }


    void close() throws SQLException {
        prstmt.executeBatch();
        prstmt.close();
    }
}
