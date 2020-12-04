package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private Diary diary = Diary.getInstance();
    private CountDownLatch latch;
    private CountDownLatch termLatch;

    public LandoMicroservice(CountDownLatch latch,CountDownLatch termLatch) {
        super("Lando");
        this.latch = latch;
        this.termLatch = termLatch;
    }

    @Override
    protected void initialize() {
       subscribeEvent(BombDestroyerEvent.class, (BombDestroyerEvent event) -> {
           System.out.println("Lando bombing the Death Star!");
           try {
               Thread.sleep(event.getDuration());
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           complete(event, true);
       });

        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast broadcast) -> {
            terminate();
            diary.setTerminateTime(this,System.currentTimeMillis());
            termLatch.countDown();
        });
        latch.countDown();
    }
}
