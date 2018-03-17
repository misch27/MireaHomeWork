import java.util.ArrayList;

public class SourceJSON {
    private ArrayList<String> mainStatistic = new ArrayList<>();
    private ArrayList<String> ordersAndStory = new ArrayList<>();
    private ArrayList<String> chat = new ArrayList<>();
    private boolean isIntegrity;
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
