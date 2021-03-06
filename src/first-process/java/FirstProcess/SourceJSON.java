package FirstProcess;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SourceJSON {
    @SerializedName("main")
    private ArrayList<String> mainStatistic = new ArrayList<>();
    @SerializedName("ordersAndStory")
    private ArrayList ordersAndStory = new ArrayList<>();
    @SerializedName("chat")
    private ArrayList<String> chat = new ArrayList<>();

    public ArrayList<String> getMainStatistic() {
        return mainStatistic;
    }

    public void setMainStatistic(ArrayList<String> mainStatistic) {
        this.mainStatistic = mainStatistic;
    }

    public ArrayList<String> getOrdersAndStory() {
        return ordersAndStory;
    }

    public void setOrdersAndStory(ArrayList<String> ordersAndStory) {
        this.ordersAndStory = ordersAndStory;
    }

    public ArrayList<String> getChat() {
        return chat;
    }

    public void setChat(ArrayList<String> chat) {
        this.chat = chat;
    }

}
