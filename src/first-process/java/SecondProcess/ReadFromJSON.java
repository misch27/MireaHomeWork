package SecondProcess;

import SecondProcess.ControllerArea.Controller;
import SecondProcess.DAO.DAOThreadDemon;
import com.google.gson.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class ReadFromJSON extends Thread {

    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private Controller controller;

    public ReadFromJSON(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run(){
        try {
            SingletonCl singletonCl = SingletonCl.getInstance();
            ArrayList<SourceJSON> readList = singletonCl.getSources();
            int numberToStart = readList.size();
            String stingFile = "";

            try {
                stingFile = read();
            } catch (IOException e) {
                e.printStackTrace();
            }


            JsonParser parser = new JsonParser();
            JsonArray jsonValuta = (JsonArray) parser.parse(stingFile);


            for (int i = readList.size(); i < jsonValuta.size(); i++) {
                SourceJSON sourceJSON = new SourceJSON();

                JsonObject itemValuta = (JsonObject) jsonValuta.get(i);
                ArrayList<String> mainStatistic
                        = writeToArray((JsonArray) itemValuta.get("main"));
                ArrayList<ArrayList<String>> ordersAndStory
                        = writeToArrayTwoLevel((JsonArray) itemValuta.get("ordersAndStory"));
                ArrayList<String> chat
                        = writeToArray((JsonArray) itemValuta.get("chat"));

                sourceJSON.setMainStatistic(mainStatistic);
                sourceJSON.setOrdersAndStory(ordersAndStory);
                sourceJSON.setChat(chat);
                readList.add(sourceJSON);
            }
            singletonCl.setSources(readList);
            parsForDAO(numberToStart, readList);
            controller.insertValues(true);

        }catch (ClassCastException clc){
            System.out.println("Пустой файл");
            controller.insertValues(false);
        }
    }

    private ArrayList<String> writeToArray(JsonArray jsonArray) {
            ArrayList<String> newArray = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                newArray.add(jsonArray.get(i).getAsString());
            }
            return newArray;
        }


    private ArrayList<ArrayList<String>> writeToArrayTwoLevel(JsonArray jsonArray) {
            ArrayList<ArrayList<String>> newArray = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonArray jsonInputArray = (JsonArray) jsonArray.get(i);
                ArrayList<String> val = new ArrayList<>();
                for (int j = 0; j < jsonInputArray.size(); j++) {
                    val.add(jsonInputArray.get(j).getAsString());

                }
                newArray.add(val);

            }
            return newArray;
        }

    private String decodeUTF8(byte[] bytes) {
        return new String(bytes, UTF8_CHARSET);
    }

    private final String fileLocation = System.getProperty("user.dir") + "\\arr.json";
    private String read() throws IOException {        //Create file object
        try {
            RandomAccessFile rafile = new RandomAccessFile(fileLocation, "rw");
                FileChannel fileChannel = rafile.getChannel();
                FileLock lock = fileChannel.lock();
                MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, rafile.length());
                byte[] b = new byte[(int) rafile.length()];
                for (int i = 0; i < rafile.length(); i++) {
                    b[i] = buffer.get(i);
                }
                buffer.force();
                lock.release();
                fileChannel.close();
                rafile.close();
                return decodeUTF8(b);

        }catch (IndexOutOfBoundsException exep){
            return read();
        }
    }
    private void parsForDAO(int numberToStart, ArrayList<SourceJSON> listJSON){
        DAOThreadDemon threadDemon = new DAOThreadDemon(numberToStart, listJSON);
        threadDemon.setDaemon(true);
        threadDemon.start();
    }

}
