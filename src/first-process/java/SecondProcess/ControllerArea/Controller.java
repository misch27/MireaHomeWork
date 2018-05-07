package SecondProcess.ControllerArea;

import SecondProcess.ReadFromJSON;
import SecondProcess.SingletonCl;
import SecondProcess.SourceJSON;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Controller {
    private SingletonCl singletonCl;
    public Controller() {

        singletonCl = SingletonCl.getInstance();

    }

    @FXML
    private Button update;

    @FXML
    private TableView sell, buy, history;

    @FXML
    private TableColumn<Sell, String > sellPrice, sellDASH, sellBTC;

    @FXML
    private TableColumn<Buy, String> buyPrice, buyDASH, buyBTC;

    @FXML
    private TableColumn<History, String> historyTime, historyPrice, historyDASH;

    @FXML
    private ListView<String> list, chat;

    @FXML
    private Text Name, Last, High, Low, V;

    @FXML
    private void initialize(ObservableList<String> obsList){
        buyPrice.setCellValueFactory(new PropertyValueFactory<Buy, String>("price"));
        buyBTC.setCellValueFactory(new PropertyValueFactory<Buy, String>("BTC"));
        buyDASH.setCellValueFactory(new PropertyValueFactory<Buy, String>("DASH"));
        sellPrice.setCellValueFactory(new PropertyValueFactory<Sell, String>("price"));
        sellBTC.setCellValueFactory(new PropertyValueFactory<Sell, String>("BTC"));
        sellDASH.setCellValueFactory(new PropertyValueFactory<Sell, String>("DASH"));
        historyTime.setCellValueFactory(new PropertyValueFactory<History, String>("time"));
        historyPrice.setCellValueFactory(new PropertyValueFactory<History, String>("price"));
        historyDASH.setCellValueFactory(new PropertyValueFactory<History, String>("DASH"));
        list.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                                        String old_val, String new_val) {
                        chooseAnotherValuta(new_val);
                    }
                });


    }
    public void updateValues(ActionEvent actionEvent) {
        update.setDisable(true);
            ReadFromJSON readFromJSON = new ReadFromJSON(this);
            readFromJSON.setName("ReadThread");
            readFromJSON.start();
    }
    public void insertValues(boolean good){
        if(good) {
            try {
                ObservableList<String> listObserver = FXCollections.observableArrayList();
                ArrayList<SourceJSON> listCollection = singletonCl.getSources();
                for (SourceJSON sourceJSON : listCollection) {
                    listObserver.add(sourceJSON.mainStatistic.get(0));
                }
                if (list.isFocused()) {
                    update.requestFocus();
                }
                initialize(listObserver);

                list.setItems(listObserver);
            }catch (IllegalStateException illeg){
                System.out.println("Получен доступ из другого потока");
            }
            setChatMesseges();
            System.out.println("Данные обновлены");

        }
        update.setDisable(false);
    }

    private void setChatMesseges(){
        ObservableList<String> listChat = FXCollections.observableArrayList();
        SingletonCl singletonCl = SingletonCl.getInstance();
        ArrayList<SourceJSON> sources = singletonCl.getSources();
        LinkedHashSet<String> messeges = new LinkedHashSet<>();
        for (SourceJSON source : sources) {
            for (String s : source.getChat()) {
                messeges.add(s);
            }
        }
        listChat.addAll(messeges);
        chat.setDisable(true);
        chat.setItems(listChat);
        chat.setDisable(false);
    }

    private void chooseAnotherValuta(String newValue){
        boolean isNull = true;
        SourceJSON sourceJSON = new SourceJSON();
        ObservableList<Buy> listBuy;
        ObservableList<Sell> listSell;
        ObservableList<History> listHistory;
        SingletonCl singletonCl = SingletonCl.getInstance();
        for (SourceJSON s:
             singletonCl.getSources()) {
            if(s.mainStatistic.get(0).equals(newValue)){
                sourceJSON = s;
                isNull = false;
                break;
            }
        }
//        ObservableList<Buy> buyGlobList = FXCollections.observableArrayList();
//        ObservableList<Buy> sellGlobList = FXCollections.observableArrayList();
//        ObservableList<Buy> historyGlobList = FXCollections.observableArrayList();
        if(!isNull) {
            setMainInform(sourceJSON);
            listBuy = buyVals(sourceJSON);
            buy.setItems(listBuy);
            listSell = sellVals(sourceJSON);
            sell.setItems(listSell);
            listHistory = historyVals(sourceJSON);
            history.setItems(listHistory);
        }

    }
    private ObservableList<Buy> buyVals(SourceJSON sourceJSON){
        ObservableList<Buy> listObserver = FXCollections.observableArrayList();
        for (String s : sourceJSON.getOrdersAndStory().get(1)) {
            Buy buy = new Buy();
            buy.setPrice(s.split(" ")[0]);
            buy.setDASH(s.split(" ")[1]);
            buy.setBTC(s.split(" ")[2]);
            listObserver.add(buy);
        }
        return listObserver;
    }
    private ObservableList<Sell> sellVals(SourceJSON sourceJSON){
        ObservableList<Sell> listObserver = FXCollections.observableArrayList();
        for (String s : sourceJSON.getOrdersAndStory().get(0)) {
            Sell sell = new Sell();
            sell.setPrice(s.split(" ")[0]);
            sell.setDASH(s.split(" ")[1]);
            sell.setBTC(s.split(" ")[2]);
            listObserver.add(sell);
        }
        return listObserver;
    }
    private ObservableList<History> historyVals(SourceJSON sourceJSON){
        ObservableList<History> listObserver = FXCollections.observableArrayList();
        for (String s : sourceJSON.getOrdersAndStory().get(2)) {
            History history = new History();
            history.setTime(s.split(" ")[0]);
            history.setPrice(s.split(" ")[1]+" "+s.split(" ")[2]);
            history.setDASH(s.split(" ")[3]);
            listObserver.add(history);
        }
        return listObserver;
    }
    private void setMainInform(SourceJSON sourceJSON){
        Name.setText(sourceJSON.mainStatistic.get(0));
        Last.setText(sourceJSON.mainStatistic.get(1));
        High.setText(sourceJSON.mainStatistic.get(2));
        Low.setText(sourceJSON.mainStatistic.get(3));
        V.setText(sourceJSON.mainStatistic.get(4));
    }

}
