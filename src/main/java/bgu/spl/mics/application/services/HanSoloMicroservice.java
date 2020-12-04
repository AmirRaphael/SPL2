package bgu.spl.mics.application.services;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.concurrent.CountDownLatch;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private Ewoks ewoks = Ewoks.getInstance();
    private Diary diary = Diary.getInstance();
    private CountDownLatch latch;
    private CountDownLatch termLatch;

    public HanSoloMicroservice(CountDownLatch latch, CountDownLatch termLatch) {
        super("Han");
        this.latch = latch;
        this.termLatch = termLatch;
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, (AttackEvent event) -> {
            Attack attack = event.getAttack();
            System.out.println(getName() + " acquiring ewkos!");
            ewoks.acquireEwoks(attack.getSerials());
            System.out.println(getName() + " attacking!");
            try {
                Thread.sleep(attack.getDuration());
            } catch (InterruptedException ignored){}
            ewoks.releaseEwoks(attack.getSerials());
            complete(event, true);
            diary.setFinishTime(this, System.currentTimeMillis());
            diary.incTotalAttacks(this);
        });

        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast broadcast) -> {
            terminate();
            diary.setTerminateTime(this,System.currentTimeMillis());
            termLatch.countDown();
        });
        latch.countDown();
    }
}
