public class Consumer extends applicationThread {

    public Consumer(CircularBuffer buffer, int waitTime) {
        super(buffer, waitTime);
    }

    public void run() {
        for (int i = 1; i <= 10; i++) {
            try {
                Thread.sleep(waitTime);
                buffer.pull();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
        System.out.println("Terminating Consumer.");
    } 
}
