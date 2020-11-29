package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    public LandoMicroservice(long duration) {
        super("Lando");
    }

    @Override
    protected void initialize() {
       subscribeEvent(BombDestroyerEvent.class, (BombDestroyerEvent event) -> {
           try {
               Thread.sleep(event.getDuration());
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           complete(event, true);
       });

        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast broadcast) -> {
            //todo: implement termination broadcast callback
        });
    }
}
