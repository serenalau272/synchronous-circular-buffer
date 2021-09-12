import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class CircularBuffer {
    // NOTE: this class includes multiple console printing commands for testing
    // purposes

    private Lock m_Mutex = new ReentrantLock();
    private final int MAX_SIZE;
    private Object[] m_Buffer;
    private int m_WriteOffset;
    private int m_ReadOffset;
    private int numOccupied = 0;

    private Condition canPush = m_Mutex.newCondition();
    private Condition canPull = m_Mutex.newCondition();

    public CircularBuffer(int size) {
        m_WriteOffset = 0;
        m_ReadOffset = 0;
        MAX_SIZE = size;
        m_Buffer = new Object[MAX_SIZE];
    }

    public void CircularBufferDestructor() {

    }

    public void push(Object val) throws InterruptedException {
        // lock access to buffer
        m_Mutex.lock();
        try {
            // block producer thread when buffer is full
            while (numOccupied == MAX_SIZE) {
                System.out.println("Buffer full. Producer waiting.");
                // wait until a buffer space is free, signalled by a consumer pulling
                canPush.await();
            }
            // write to buffer at next space
            m_Buffer[m_WriteOffset] = val;
            System.out.println();
            System.out.println("Producer pushes " + val);

            // update the writer index
            m_WriteOffset = (m_WriteOffset + 1) % MAX_SIZE;
            // increment number of buffer spaces occupied
            numOccupied++;
            bufferCurrentStateInfo();
            // signal that the buffer is no longer empty and the consumer can pull
            canPull.signal();
        } finally {
            // allow (unlock) access to buffer
            m_Mutex.unlock();
        }
    }

    public Object pull() throws InterruptedException {
        // declare object to be pulled and returned to the consumer
        Object objectPulled;
        // lock access to buffer
        m_Mutex.lock();

        try {
            // block consumer thread when buffer is empty
            while (numOccupied == 0) {
                System.out.println("Buffer empty. Consumer waiting.");
                // wait until the buffer is written to, signalled by a producer pushing
                canPull.await();
            }
            // read from buffer and save object
            objectPulled = m_Buffer[m_ReadOffset];
            // remove object read from buffer
            m_Buffer[m_ReadOffset] = null;
            System.out.println();
            System.out.println("Consumer pulls " + objectPulled);
            // update reader index
            m_ReadOffset = (m_ReadOffset + 1) % MAX_SIZE;
            // decrement number of buffer spaces occupied
            numOccupied--;
            bufferCurrentStateInfo();
            // signal that the buffer is no longer full and the producer can push
            canPush.signal();
        } finally {
            // allow (unlock) access to buffer
            m_Mutex.unlock();
        }
        // return pulled object to the consumer
        return objectPulled;
    }

    public void bufferCurrentStateInfo() {
        // This method simply prints all the current objects in the buffer as a String
        // representation, or null if there is no object in the buffer space
        System.out.println("Buffer: ");
        for (Object object : m_Buffer) {
            try {
                System.out.print(object.toString() + " ");
            } catch (NullPointerException e) {
                System.out.print("null ");
            }
        }
        System.out.println();

    }

}