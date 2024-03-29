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
    private List<Ewok> ewoks;

    private static class singletonHolder { private static final Ewoks instance = new Ewoks(); }

    private Ewoks() {
        ewoks = new ArrayList<Ewok>();
    }

    public static Ewoks getInstance() {
    return singletonHolder.instance;
}

    public void acquireEwoks(List<Integer> serials) {
        // Note that the "serials" list is sorted
        for (int serialNum : serials) {
            ewoks.get(serialNum-1).acquire();
        }
    }

    public void releaseEwoks(List<Integer> serials) {
        for (int serialNum : serials) {
            ewoks.get(serialNum-1).release();
        }
    }

    public void createEwoks(int numOfEwoks) {
        for (int i = 1; i <= numOfEwoks; i++) {
            ewoks.add(new Ewok(i));
            // Notice : ewoks[j] = Ewok with serial number j+1
        }
    }
}
