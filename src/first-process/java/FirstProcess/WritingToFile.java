package FirstProcess;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferOverflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

class WritingToFile extends Thread {
    private final String fileLocation = System.getProperty("user.dir") + "\\arr.json";
    private String s;
    private int currentNum;
    private long correctFileSize;
    WritingToFile(String s, int currentNum) {
        this.s = s;
        this.currentNum = currentNum;
    }
    boolean insertS(){
        try {
            if(currentNum == 0) {
                s = "[" + s + " ]";
                correctFileSize = s.getBytes().length; //для [] в json
            }else{
                s = "," + s + ']';
                correctFileSize = s.getBytes().length; //сам текст + ',' в начале
            }
            try(RandomAccessFile rafile = new RandomAccessFile(fileLocation, "rw")) {
                FileChannel fileChannel = rafile.getChannel();
                FileLock lock = fileChannel.lock();
                MappedByteBuffer mbb;
                if (currentNum == 0) {
                    mbb = fileChannel.map(FileChannel.MapMode.READ_WRITE, rafile.length(), correctFileSize);
                } else {
                    mbb = fileChannel.map(FileChannel.MapMode.READ_WRITE, rafile.length()-1, correctFileSize);
                }
                mbb.put(s.getBytes());
                mbb.force();
                lock.release();
                fileChannel.close();
            }
        } catch (IOException | BufferOverflowException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
