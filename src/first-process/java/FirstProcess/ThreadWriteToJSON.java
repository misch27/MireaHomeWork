package FirstProcess;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ThreadWriteToJSON extends Thread {
    private static final Object waitObj = new Object();
    private static int count = 0;

    @Override
    public void run() {
        try {
            while (ThreadPars.getMaxNum() == 0) {
                Thread.sleep(1000);
            }
            int currentNum = 0;
            int maxVal = ThreadPars.getMaxNum();
            ArrayList<SourceJSON> sourceArr = ThreadPars.getArrJSON();
            for(currentNum = 0; currentNum<maxVal; currentNum++){
                synhronizationAllThreads();
                SourceJSON curSource = sourceArr.get(currentNum);
                Thread.sleep(500);
                writeToFile(curSource, currentNum);
            }
            System.exit(0);
        }catch (InterruptedException interrupt){
            System.out.println("Поток " + this.getName() + " завершился с ошибкой");
        }
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
