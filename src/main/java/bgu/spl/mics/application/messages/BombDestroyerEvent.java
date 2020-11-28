package bgu.spl.mics.application.messages;

public class BombDestroyerEvent {
    private long duration;

    public BombDestroyerEvent(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}
