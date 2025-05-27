import java.util.*;
import java.util.concurrent.*;

class TaskStatus {
    enum Status {RUNNING, COMPLETED, TIMED_OUT}
    static final ConcurrentHashMap<Integer, Status> statusMap = new ConcurrentHashMap<>();
}

class MonitoredTask implements Runnable {
    private final int id;
    private final long duration;
    private final Object monitor;
    private volatile boolean interrupted = false;

    public MonitoredTask(int id, long duration, Object monitor) {
        this.id = id;
        this.duration = duration;
        this.monitor = monitor;
        TaskStatus.statusMap.put(id, TaskStatus.Status.RUNNING);
    }

    @Override
    public void run() {
        System.out.println("Task " + id + " started.");
        synchronized (monitor) {
            try {
                Thread.sleep(duration);
                if (!interrupted) {
                    synchronized (TaskStatus.statusMap) {
                        TaskStatus.statusMap.put(id, TaskStatus.Status.COMPLETED);
                    }
                    System.out.println("Task " + id + " completed.");
                }
            } catch (InterruptedException e) {
                TaskStatus.statusMap.put(id, TaskStatus.Status.TIMED_OUT);
                System.out.println("Task " + id + " timed out.");
            }
        }
    }

    public void interrupt() {
        interrupted = true;
    }
}

class Watchdog extends Thread {
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
                System.out.println("===== Watchdog =====");
                TaskStatus.statusMap.forEach((id, status) ->
                        System.out.printf("Task %d: %s\n", id, status));
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}