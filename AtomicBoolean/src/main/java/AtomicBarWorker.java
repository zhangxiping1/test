import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
/*

   让多个线程    在运行的同一块代码   如果想让这块代码中的一小块代码同一时间只有一个线程运行    就需要 if (exists.compareAndSet(false, true)) {



 */
public  class AtomicBarWorker implements Runnable {

    //private static AtomicBoolean exists = new AtomicBoolean(false);
    private static AtomicBoolean exists = new AtomicBoolean(false);
    //private  Boolean exists =true;
    private String name;

    public AtomicBarWorker(String name) {
        this.name = name;
    }

    public void run() {
        for (int i=0;i<5;i++){
            if (!exists.getAndSet( true)) {

                System.out.println(name + " enter");
                try {
                    System.out.println(name + " working");
                    //TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    // do nothing
                }
                System.out.println(name + " leave");
                exists.set(false);

            } else {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(name + " give up");
            }

        }
    }


    public static void main(String[] args) {

         new Thread(new AtomicBarWorker("111")).start();
         new Thread(new AtomicBarWorker("222")).start();
         new Thread(new AtomicBarWorker("333")).start();

    }

}