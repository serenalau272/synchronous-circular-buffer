public abstract class applicationThread implements Runnable {
    // This is abstract class forms the blueprint for the Consumer and Producer
    // threads
    
    protected CircularBuffer buffer;
    protected int waitTime;

    public applicationThread(CircularBuffer buffer, int waitTime) {
        this.buffer = buffer;
        this.waitTime = waitTime;
    }

    public abstract void run();
}