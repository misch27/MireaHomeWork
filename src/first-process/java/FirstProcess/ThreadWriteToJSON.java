package FirstProcess;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ThreadWriteToJSON extends Thread {
    private static final Object waitObj = new Object();
    private static int count = 0;

    @Override
    public void run() {
        while (ThreadPars.getMaxNum()==0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int currentNum = 0;
        int maxVal = ThreadPars.getMaxNum();
        ArrayList<SourceJSON> sourceArr = ThreadPars.getArrJSON();

        while (currentNum< maxVal){
            try {
            synhronizationAllThreads();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(sourceArr.size()<currentNum){
                sourceArr = ThreadPars.getArrJSON();
                continue;
            }
            try {
                SourceJSON curSource = sourceArr.get(currentNum);
                    Thread.sleep(500);
                    writeToFile(curSource, currentNum);
            }catch (IndexOutOfBoundsException | InterruptedException ex){
                System.out.println("Этот костыль точно работает?");
                continue;
            }
            currentNum++;
        }
        System.exit(0);
    }

    private void writeToFile(SourceJSON sourceJSON, int currentNum){
        Gson gson = new Gson();
        WritingToFile writing = new WritingToFile(gson.toJson(sourceJSON),currentNum);

        if(writing.insertS()) {
            System.out.println(gson.toJson(sourceJSON));
        }
    }

    void synhronizationAllThreads() throws InterruptedException {
        synchronized (waitObj) {
            if(count == 4){
                count = 0;
            }
            count++;
            waitObj.notifyAll();
            while (count<4) {
                if (count < 4) {
                    waitObj.wait();
                }
            }
        }
    }
}
