package SecondProcess.DAO;

import SecondProcess.SourceJSON;

import java.sql.SQLException;
import java.util.ArrayList;

public class DAOThreadDemon extends Thread {
    private int numberToStart;
    private ArrayList<SourceJSON> listJSON;
    public DAOThreadDemon(int numberToStart, ArrayList<SourceJSON> listJSON) {
        this.numberToStart = numberToStart;
        this.listJSON = listJSON;
    }

    @Override
    public void run() {
        DAO dao = new DAO();
        for (int i = numberToStart; i<listJSON.size(); i++){
            try {
                dao.setTabName("main_table");
                dao.batchSQL(i, listJSON.get(i).getMainStatistic());
                dao.setTabName("sell");
                dao.batchSQL(i, listJSON.get(i).getOrdersAndStory().get(0));
                dao.setTabName("buy");
                dao.batchSQL(i, listJSON.get(i).getOrdersAndStory().get(1));
                dao.setTabName("story");
                dao.batchSQL(i, listJSON.get(i).getOrdersAndStory().get(2));
                dao.setTabName("Chat");
                dao.batchSQL(i, listJSON.get(i).getChat());
            }catch (SQLException sql){
                sql.printStackTrace();
            }
        }
        System.out.println("База данных отработала");
    }
}
