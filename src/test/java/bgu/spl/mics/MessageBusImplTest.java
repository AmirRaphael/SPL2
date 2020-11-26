package bgu.spl.mics;

import bgu.spl.mics.application.messages.DemoBroadcast;
import bgu.spl.mics.application.messages.DemoEvent;
import bgu.spl.mics.application.services.TestMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBus messageBus;
    private MicroService microService1;
    private MicroService microService2;
    private Broadcast broadcast;
    private Event<Boolean> event1;
    private Event<Boolean> event2;



    @BeforeEach
    void setUp() {
        messageBus = MessageBusImpl.getInstance();
        microService1 = new TestMicroservice();
        microService2 = new TestMicroservice();
        messageBus.register(microService1);
        messageBus.register(microService2);
        microService1.initialize();
        microService2.initialize();
        broadcast = new DemoBroadcast();
        event1 = new DemoEvent();
        event2 = new DemoEvent();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void subscribeEvent() {
    }

    @Test
    void subscribeBroadcast() {
    }

    @Test
    void complete() {
        Future<Boolean> future = messageBus.sendEvent(event1);
        messageBus.complete(event1, true);
        assertTrue(future.get());
    }

    @Test
    void sendBroadcast() {
        messageBus.sendBroadcast(broadcast);
        try {
            assertEquals(broadcast, messageBus.awaitMessage(microService1));
            assertEquals(broadcast, messageBus.awaitMessage(microService2));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void sendEvent() {
        Future<Boolean> future1 = messageBus.sendEvent(event1);
        Future<Boolean> future2 = messageBus.sendEvent(event2);
        try {
            // Assuming microService1 will get event1 & Assuming microService2 will get event2
            assertEquals(event1, messageBus.awaitMessage(microService1));
            assertEquals(event2, messageBus.awaitMessage(microService2));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void register() {
    }

    @Test
    void unregister() {
    }

    @Test
    void awaitMessage() {
        messageBus.sendBroadcast(broadcast);
        try {
            assertEquals(broadcast, messageBus.awaitMessage(microService1));
        } catch (Exception e) {
            fail();
        }
    }
}