package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus { // TODO check impl to be thread safe and singleton!!!\

	private static final MessageBusImpl instance = new MessageBusImpl();
	private Map<MicroService, Queue<Message>> messageQueues;
	private Map<Class<? extends Message>,Set<MicroService>> messageMap;


	//TODO: maybe we need another map for matching event types to microservices.

	private MessageBusImpl(){ // private constructor, as the class is a singleton. TODO implement
		this.messageQueues = new HashMap<>();
		this.messageMap = new HashMap<>(); // TODO: for now, the messages each microservice subscribes to are stored in one map, both event and broadcast.
	}
	public static MessageBus getInstance(){
		return instance;
	}

	
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		
        return null;
	}

	@Override
	public void register(MicroService m) {
		this.messageQueues.put(m,new LinkedList<>()); //TODO: check if concurrent container needed.

	}

	@Override
	public void unregister(MicroService m) {
		
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		
		return null;
	}
}
