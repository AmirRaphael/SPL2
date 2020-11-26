package bgu.spl.mics;

import bgu.spl.mics.application.messages.DemoBroadcast;
import bgu.spl.mics.application.messages.DemoEvent;
import bgu.spl.mics.application.services.TestMicroservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBus messageBus;
    Thread m1;
    Thread m2;


    @BeforeEach
    void setUp() {
        m1 = new Thread(new TestMicroservice());
        m2 = new Thread(new TestMicroservice());
        m1.start();
        m2.start();
    }

    @Test
    void subscribeEvent() {
    }

    @Test
    void subscribeBroadcast() {
    }

    @Test
    void complete() {
    }

    @Test
    void sendBroadcast() {




    }

    @Test
    void sendEvent() {
    }

    @Test
    void register() {
    }

    @Test
    void unregister() {
    }

    @Test
    void awaitMessage() {
    }
}