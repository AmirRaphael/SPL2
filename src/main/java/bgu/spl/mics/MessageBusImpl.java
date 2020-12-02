package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;//TODO : explain!

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus { // TODO check impl to be thread safe and singleton!!!

	private Map<MicroService, LinkedBlockingQueue<Message>> messageQueues;
	private Map<Class<? extends Event<?>>,Queue<MicroService>> eventMap;
	private Map<Class<? extends Broadcast>,Set<MicroService>> broadMap;
	private Map<Event<?>,Future> futureMap;
	private final Object roundRobinLock = new Object();

	//TODO: maybe we need another map for matching event types to microservices.


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
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(!messageQueues.containsKey(m)){
			throw new IllegalStateException("Microservice m is not registered!");
		}
		if(!this.eventMap.containsKey(type)){
			this.eventMap.put(type,new LinkedBlockingQueue<>()); //TODO : check prefered container
		}
		this.eventMap.get(type).add(m);

		
	}

	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(!messageQueues.containsKey(m)){
			throw new IllegalStateException("Microservice m is not registered!");
		}
		if(!this.broadMap.containsKey(type)){
			this.broadMap.put(type,ConcurrentHashMap.newKeySet());
		}
		this.broadMap.get(type).add(m);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		futureMap.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for(MicroService m : broadMap.get(b.getClass())){
			this.messageQueues.get(m).add(b);
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (roundRobinLock){
			Queue<MicroService> eventQueue = eventMap.get(e.getClass());
			MicroService receiver  = eventQueue.poll();
			if(receiver!=null){
				messageQueues.get(receiver).add(e);
				eventQueue.add(receiver);
				Future<T> future = new Future<>();
				futureMap.put(e,future);
				return future;
			}
		}
		return null;
	}

	@Override
	public void register(MicroService m) {
		this.messageQueues.put(m,new LinkedBlockingQueue<>()); //TODO: check if concurrent container needed.
		System.out.println(m.getName()+" registered!");
	}

	@Override
	public void unregister(MicroService m) {
		this.messageQueues.remove(m);
		for (Class<? extends Event> e : eventMap.keySet()){
			eventMap.get(e).remove(m);
		}
		for (Class<? extends Broadcast> b : broadMap.keySet()){
			broadMap.get(b).remove(m);
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		try{
			return messageQueues.get(m).take();
		} catch(InterruptedException ignored  ){} //TODO: check what needs to happen here
		return null;
	}
}
