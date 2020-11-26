package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DemoBroadcast;

public class TestMicroservice extends MicroService {
    public TestMicroservice (){
        super("Test");
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(DemoBroadcast.class,(DemoBroadcast a)-> System.out.println("Broadcast Message"));
    }
}
