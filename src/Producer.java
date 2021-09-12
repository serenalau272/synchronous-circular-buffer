public class Producer extends applicationThread {

    public Producer(CircularBuffer buffer, int waitTime) {
        super(buffer, waitTime);
    }

    @Override
    public void run() {
        for (double i = 1; i <= 10; i++) {
            try {
                Thread.sleep(waitTime);
                buffer.push(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
        System.out.println("Terminating Producer.");
    } 
}