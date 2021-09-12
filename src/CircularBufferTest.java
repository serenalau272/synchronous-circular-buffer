import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CircularBufferTest {
// This class shows my CircularBuffer solution is correct and allows for testing: 
// - When the producer pushes data at a faster rate than the consumer pulls 
// - When the consumer pulls data at a faster rate than the producer pushes


    public static void main(String[] args) {
        ExecutorService application = Executors.newFixedThreadPool(2);
        // Example max size:
        int max = 5;
        CircularBuffer buffer = new CircularBuffer(max);

        // Test Producer faster than Consumer
        
        System.out.println("-----------------------------");
        System.out.println("Producer faster than Consumer");
        System.out.println("-----------------------------");
        int producerWaitTime = 500;
        int consumerWaitTime = 2000;
        

        // Test Consumer faster than Producer
        /*
        System.out.println("-----------------------------");
        System.out.println("Consumer faster than Producer");
        System.out.println("-----------------------------");
        int producerWaitTime = 2000;
        int consumerWaitTime = 500;
        */

        try {
            application.execute(new Producer(buffer, producerWaitTime));
            application.execute(new Consumer(buffer, consumerWaitTime));
        } catch (Exception e) {
            e.printStackTrace();
        } 

        application.shutdown();
    }
}
