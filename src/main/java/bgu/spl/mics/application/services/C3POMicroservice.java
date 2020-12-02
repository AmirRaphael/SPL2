package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.concurrent.CountDownLatch;

/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    private Ewoks ewoks = Ewoks.getInstance();
    private Diary diary = Diary.getInstance();
    private CountDownLatch latch;
	
    public C3POMicroservice(CountDownLatch latch) {
        super("C3PO");
        this.latch=latch;
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, (AttackEvent event) -> {
            Attack attack = event.getAttack();
            ewoks.acquireEwoks(attack.getSerials()); //todo: make this concurrent
            try {
                Thread.sleep(attack.getDuration());
            } catch (InterruptedException ignored){} //todo: check this fucking exception shit
            ewoks.releaseEwoks(attack.getSerials());
            complete(event, true);
            diary.setFinishTime(this, System.currentTimeMillis());
            diary.incTotalAttacks(this);
        });

        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast broadcast) -> {
            terminate();
            diary.setTerminateTime(this,System.currentTimeMillis());
        });
        latch.countDown();
    }
}
