package bgu.spl.mics.application.passiveObjects;


import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private static final Object LOCK = new Object();
    private List<Ewok> ewoks;

    private static class singletonHolder { private static final Ewoks instance = new Ewoks(); }

    private Ewoks() {
        ewoks = new ArrayList<Ewok>();
    }

    public static Ewoks getInstance() {
    return singletonHolder.instance;
}

    //todo: implement in a concurrent thread-safe way (this should be a blocking method)
    public void acquireEwoks(List<Integer> serials) {
        synchronized (LOCK) {
            for (int serialNum : serials) {
                while (!ewoks.get(serialNum-1).isAvailable()) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException ignored){}
                }
                ewoks.get(serialNum-1).acquire();
            }
        }
    }

    //todo: implement in a concurrent thread-safe way (this should be a blocking method)
    public void releaseEwoks(List<Integer> serials) {
        synchronized (LOCK) {
            for (int serialNum : serials) {
                ewoks.get(serialNum-1).release();
            }
            LOCK.notifyAll();
        }
    }

    public void createEwoks(int numOfEwoks) {
        for (int i = 1; i <= numOfEwoks; i++) {
            ewoks.add(new Ewok(i));
            // Notice : ewoks[j] = Ewok with serial number j+1
        }
    }
}
