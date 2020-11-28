package bgu.spl.mics.application.services;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.Terminate;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewoks;

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

    public HanSoloMicroservice() {
        super("Han");
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, (AttackEvent event) -> {
            Attack attack = event.getAttack();
            ewoks.acquireEwoks(attack.getSerials()); //todo: make this concurrent
            try {
                Thread.sleep(attack.getDuration());
            } catch (InterruptedException ignored){} //todo: check this fucking exception shit
            complete(event, true);
        });

        subscribeBroadcast(Terminate.class, (Terminate terminate) -> {
            //todo: implement termination broadcast callback
        });
    }
}
