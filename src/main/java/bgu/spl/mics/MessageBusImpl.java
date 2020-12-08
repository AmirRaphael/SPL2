package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private final Map<MicroService, LinkedBlockingQueue<Message>> messageQueues;
	private final Map<Class<? extends Event<?>>, Queue<MicroService>> eventMap;
	private final Map<Class<? extends Broadcast>, Set<MicroService>> broadMap;
	private final Map<Event<?>, Future> futureMap;

	private final Object roundRobinLock = new Object();
	private static final int MAX_PERMITS = 100;	// for the Star-Wars program - only 5 permits are needed
	private final Semaphore semaphore = new Semaphore(MAX_PERMITS, true);

	private static class SingletonHolder{
		private static final MessageBusImpl instance = new MessageBusImpl();
	}

	public static MessageBus getInstance(){
		return SingletonHolder.instance;
	}

	private MessageBusImpl(){ // private constructor, as the class is a singleton.
		this.messageQueues = new ConcurrentHashMap<>();
		this.eventMap = new ConcurrentHashMap<>();
		this.broadMap = new ConcurrentHashMap<>();
		this.futureMap = new ConcurrentHashMap<>();
	}
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(!messageQueues.containsKey(m)){
			throw new IllegalStateException("MicroService " + m.getName() + " is not registered!");
		}

		synchronized (eventMap) { // Avoids duplicate put() method call for the same "type"
			if (!this.eventMap.containsKey(type)) {
				this.eventMap.put(type, new LinkedBlockingQueue<MicroService>());
			}
		}
		Queue<MicroService> microServiceQueue = this.eventMap.get(type);
		microServiceQueue.add(m);

		semaphore.release();
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if(!messageQueues.containsKey(m)){
			throw new IllegalStateException("MicroService " + m.getName() + " is not registered!");
		}

		synchronized (broadMap) { // Avoids duplicate put() method call for the same "type"
			if (!this.broadMap.containsKey(type)) {
				this.broadMap.put(type, ConcurrentHashMap.newKeySet());
			}
		}
		Set<MicroService> microServiceSet = this.broadMap.get(type);
		microServiceSet.add(m);

		semaphore.release();
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		futureMap.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (broadMap.get(b.getClass()) != null){
			// Adds broadcast to all the relevant MicroService message-queues
			for(MicroService microService : broadMap.get(b.getClass())){
				Queue<Message> messageQueue = this.messageQueues.get(microService);
				messageQueue.add(b);
			}
		}

		semaphore.release();
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		try {
			semaphore.acquire();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		synchronized (roundRobinLock){ // Ensures correct execution of RoundRobin
			Queue<MicroService> eventQueue = eventMap.get(e.getClass());
			if (eventQueue != null){
				MicroService receiver = eventQueue.poll();
				if(receiver != null){
					Future<T> future = new Future<>();
					futureMap.put(e,future);
					messageQueues.get(receiver).add(e);
					eventQueue.add(receiver);
					semaphore.release();
					return future;
				}
			}
		}
		semaphore.release();
		return null;
	}

	@Override
	public void register(MicroService m) {
		this.messageQueues.put(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		try {
			// since this method preforms modifications in all of MessageBus data-structures,
			// unregister should only execute if no other microServices are currently active (sending/subscribing)
			semaphore.acquire(MAX_PERMITS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.messageQueues.remove(m);

		for (Class<? extends Event> event : eventMap.keySet()){
			Queue<MicroService> queue = eventMap.get(event);
			queue.remove(m);
		}

		for (Class<? extends Broadcast> broadcast : broadMap.keySet()){
			Set<MicroService> set = broadMap.get(broadcast);
			set.remove(m);
		}
		semaphore.release(MAX_PERMITS);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (!messageQueues.containsKey(m)){
			throw new IllegalStateException("MicroService " + m.getName() + " is not registered!");
		}
		try {
			return messageQueues.get(m).take();
		} catch(InterruptedException e) {
			System.out.println(m.getName() + " was interrupted while waiting for a message");
			throw e;
		}
	}
}
