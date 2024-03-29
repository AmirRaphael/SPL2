package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MicroService;
import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private AtomicInteger totalAttacks = new AtomicInteger(0);
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private static class singletonHolder {
        private static final Diary instance = new Diary();
    }

    private Diary() {}

    public static Diary getInstance() {
        return singletonHolder.instance;
    }

    public void setFinishTime(MicroService m, long timeStamp) {
        switch (m.getName()) {
            case "Han":
                HanSoloFinish = timeStamp;
                break;
            case "C3PO":
                C3POFinish = timeStamp;
                break;
            case "R2D2":
                R2D2Deactivate = timeStamp;
                break;
        }
    }

    public void setTerminateTime(MicroService m, long timeStamp) {
        switch (m.getName()) {
            case "Leia":
                LeiaTerminate = timeStamp;
                break;
            case "Han":
                HanSoloTerminate = timeStamp;
                break;
            case "C3PO":
                C3POTerminate = timeStamp;
                break;
            case "R2D2":
                R2D2Terminate = timeStamp;
                break;
            case "Lando":
                LandoTerminate = timeStamp;
                break;
        }
    }


    public void incTotalAttacks(MicroService m) {
        if (m.getName().equals("Han") || m.getName().equals("C3PO") ){
            totalAttacks.incrementAndGet();
        }
    }

    public void createOutputFile(String path) {
        Gson gson = new Gson();
        try{
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(gson.toJson(this));
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
