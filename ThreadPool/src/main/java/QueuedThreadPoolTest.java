import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class QueuedThreadPoolTest {

    public static void main(String[] args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("");
            }
        }).start();
        new QueuedThreadPool().start();
        new myThreadpool();
        System.out.println(11);
    }

}
