import java.util.concurrent.LinkedBlockingQueue;
/**
 * LinkedBlockingQueue实现生产者消费者模型
 */
public class LinkedBlockingQueueTest {
    //LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<String>(3);
    private int i;
    LinkedBlockingQueue<Integer> queue= new LinkedBlockingQueue(10);

    class Producer implements Runnable{
        @Override
        public void run() {
            while (true){
                try {
                    queue.put(i++);
                    //System.out.print("生产,,,,,,");
                    System.out.println("生产,,,,,,剩余容量："+queue.remainingCapacity());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Consumer implements Runnable{
        @Override
        public void run() {
            while (true){
                try {
                    queue.take();
                    System.out.println("消费,,,,剩余容量："+queue.remainingCapacity());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueueTest linkedBlockingQueueTest=new LinkedBlockingQueueTest();
        new Thread(linkedBlockingQueueTest.new Producer()).start();
        Thread.sleep(10000);
        new Thread(linkedBlockingQueueTest.new Consumer()).start();
    }
}

