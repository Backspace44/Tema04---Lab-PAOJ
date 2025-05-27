import java.util.*;

class Buffer {
    public final List<Integer> data = Collections.synchronizedList(new ArrayList<>());
}

class ProducerDeadlock implements Runnable {
    private final Buffer b1, b2;

    public ProducerDeadlock(Buffer b1, Buffer b2) {
        this.b1 = b1;
        this.b2 = b2;
    }

    @Override
    public void run() {
        synchronized (b1) {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            synchronized (b2) {
                b1.data.add(1);
                b2.data.add(2);
                System.out.println("Produced to both buffers");
            }
        }
    }
}