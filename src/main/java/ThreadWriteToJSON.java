import com.google.gson.Gson;

import java.util.ArrayList;

public class ThreadWriteToJSON extends Thread {
    private final String fileLocation = System.getProperty("user.dir") + "\\arr.json";

    @Override
    public void run() {
        int maxVal = 0;
        int currentNum = 0;
        while (ThreadPars.getMaxNum()==0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        maxVal = ThreadPars.getMaxNum();
        ArrayList<SourceJSON> sourceArr = ThreadPars.getArrJSON();
        while (currentNum<maxVal){
            if(sourceArr.size()<currentNum){
                sourceArr = ThreadPars.getArrJSON();
                continue;
            }
            try {
                SourceJSON curSource = sourceArr.get(currentNum);

                if (curSource.getIsIntegrity()) {
                    writeToFile(curSource);
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }catch (IndexOutOfBoundsException ex){continue;}
            currentNum++;
        }
    }
    private void writeToFile(SourceJSON sourceJSON){
        Gson gson = new Gson();
        System.out.println(gson.toJson(sourceJSON));

    }
}
