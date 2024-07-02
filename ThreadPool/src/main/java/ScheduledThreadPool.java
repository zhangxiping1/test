import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ScheduledThreadPool {

    public static void main(String[] args) throws Exception {
//        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
//
//        System.out.println("Current Time = " + new Date());
//
//        WorkerThread worker = new WorkerThread("do heavy processing");
//
//        scheduledThreadPool.scheduleWithFixedDelay(worker, 30, 60, TimeUnit.SECONDS);
//
//        // scheduledThreadPool.shutdown();
//        while (!scheduledThreadPool.isTerminated()) {
//        }
//        Thread.sleep(1000000);
//        System.out.println("Finished all threads");
        for (int i = 0; i < 3; i++) {
            try {
                int a = 1/(1-1);
            } catch (RuntimeException e) {
                System.out.println(i);
            } catch (Exception ex) {
                throw new Exception(ex);
            }
        }
        return;
    }

}