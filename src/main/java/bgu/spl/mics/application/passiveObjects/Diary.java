package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MicroService;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private AtomicInteger totalAttacks;
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

    public void setFinishTime(MicroService m, long value) {
        //todo: implement - each microService can update only one specific field
    }

    public void createOutputFile() {
        //todo: implement
    }
}
