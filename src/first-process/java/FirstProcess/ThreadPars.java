package FirstProcess;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

class ThreadPars extends Thread {
    private ThreadWriteToJSON thJSON;
    private static ArrayList<SourceJSON> arrJSON = new ArrayList();
    private static int maxNum = 0;
    private int num = 0;
    private int numOfThread;
    private List<String> htmlCode;
    private HtmlCodeSelenium sel;

    ThreadPars(int numOfThread, HtmlCodeSelenium sel, ThreadWriteToJSON thJSON) {
        this.thJSON = thJSON;
        this.sel = sel;
        this.numOfThread = numOfThread;
    }

    private synchronized void setValToArrJSON(int index, ArrayList<String> vals) {
        if (arrJSON.size() - 1 < index) {
            SourceJSON sourceJSON = new SourceJSON();
            ThreadPars.arrJSON.add(sourceJSON);
        }
        switch (numOfThread) {
            case 0: {
                ThreadPars.arrJSON.get(index).setMainStatistic(vals);
                break;
            }
            case 1: {
                ThreadPars.arrJSON.get(index).setOrdersAndStory(vals);
                break;
            }
            case 2: {
                ThreadPars.arrJSON.get(index).setChat(vals);
                break;
            }
        }
    }
    static ArrayList<SourceJSON> getArrJSON() {
        return arrJSON;
    }
    static int getMaxNum() {
        return maxNum;
    }

    @Override
    public void run(){
        try {

            maxNum = sel.synMaxRow();

            while (num < maxNum) {
                try {
                    setValToArrJSON(num, parsPage(htmlCode.get(num)));
                } catch (NullPointerException | IndexOutOfBoundsException ex) {
                    htmlCode = sel.getHtmlCode();
                    continue;
                }

                thJSON.synhronizationAllThreads();
                num++;
            }
        }catch (InterruptedException interrupd){
            System.out.println("Поток " + this.getName() + " завершился с ошибкой");
        }
    }

    private ArrayList parsPage(String page) {
        switch (numOfThread) {
            case 0: {
                ArrayList<String> list = new ArrayList<>();
                Document document = Jsoup.parse(page);
                Element table = document.getElementsByClass("top_center_list").get(0);
                Elements vals = table.select("li");
                vals.forEach(val -> {
                    list.add(val.text());
                });
                return list;
            }
            case 1:{
                Document document1 = Jsoup.parse(page);
                Element table1 = document1.getElementById("sellord_table");
                Elements vals1 = table1.select("tr");
                ArrayList<String> listSell = new ArrayList<>(vals1.size());
                vals1.forEach(val -> {
                    listSell.add(val.text());
                });

                Document document2 = Jsoup.parse(page);
                Element table2 = document2.getElementById("buyord_table");
                Elements vals2 = table2.select("tr");
                ArrayList<String> listBuy = new ArrayList<>(vals2.size());
                vals2.forEach(val -> {
                    listBuy.add(val.text());
                });


                Document document3 = Jsoup.parse(page);
                Element table3 = document3.getElementById("microhistory_table");
                Elements vals3 = table3.select("tr");
                ArrayList<String> listHistory = new ArrayList<>(vals3.size());
                vals3.forEach(val -> {
                    listHistory.add(val.text());
                });
                ArrayList<ArrayList<String>> ordersAndHistory = new ArrayList<>(3);
                ordersAndHistory.add(listSell);
                ordersAndHistory.add(listBuy);
                ordersAndHistory.add(listHistory);
                return ordersAndHistory;
            }
            case 2:{
                Document document = Jsoup.parse(page);
                Element table = document.getElementById("chat-list");
                Elements vals = table.select("p");
                ArrayList<String> chatList = new ArrayList<>(vals.size());
                vals.forEach(val -> {
                    chatList.add(val.text());
                });
                return chatList;
            }

        }
        return null;
    }
}
