import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class myThreadpool implements Executor {
    public BlockingQueue workqueue;
    public ArrayList<Worker> workerslist;
    int corepools;
    int max ;

    myThreadpool(){
        corepools=0;
        max=5;
        workqueue=new LinkedBlockingQueue();
        workerslist= new ArrayList(corepools);
    }

    myThreadpool(int max){
        this.corepools =0;
        this.max = max;
        workqueue=new LinkedBlockingQueue();
        workerslist= new ArrayList(corepools);

    }

    @Override
    public void execute(Runnable command) {
        if(corepools <max)
          {
           Worker w =new Worker();
           w.start();
           workerslist.add(w);
           corepools++;
        }
        workqueue.add(command);
    }


    class Worker implements Runnable{
        Thread t;

        Worker(){

            t=new Thread(this);

        }

        @Override
        public void run() {
            while (true){
                Runnable task = null;
                try {
                    task = (Runnable) workqueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.run();
            }
        }

        public void start(){
            t.start();
        }
    }

    public static void main(String[] args) {
        Object o = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程B等待获取lock锁");
                synchronized (o) {
                    System.out.println("线程B获取了lock锁");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("线程B将要运行lock.notify()方法进行通知");
                    o.notify();
                }
            }
        }).start();


        try {


            synchronized(o) {
                System.out.println("1");
                o.wait(1000);
                System.out.println("2");
            }
            System.out.println("211211222");
        } catch (InterruptedException e) {
            System.out.println("211211222");
            e.printStackTrace();
        } catch (Throwable t){
            t.printStackTrace();
            System.out.println("o");
        }

//        new myThreadpool().execute(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(11);
//            }
//        });
//        System.out.println(0>0);
    }
}
