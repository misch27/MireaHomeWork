package SecondProcess;

import java.util.ArrayList;

public class SourceJSON {
    public ArrayList<String> mainStatistic = new ArrayList<>();

    public ArrayList<ArrayList<String>> ordersAndStory = new ArrayList<>();

    public ArrayList<String> chat = new ArrayList<>();

    public ArrayList<String> getMainStatistic() {
        return mainStatistic;
    }

    public void setMainStatistic(ArrayList<String> mainStatistic) {
        this.mainStatistic = mainStatistic;
    }

    public ArrayList<ArrayList<String>> getOrdersAndStory() {
        return ordersAndStory;
    }

    public void setOrdersAndStory(ArrayList<ArrayList<String>> ordersAndStory) {
        this.ordersAndStory = ordersAndStory;
    }

    public ArrayList<String> getChat() {
        return chat;
    }

    public void setChat(ArrayList<String> chat) {
        this.chat = chat;
    }


}
