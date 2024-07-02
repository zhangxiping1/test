import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentTest {

  public static void concurrent(int threadNum,Runnable runnable){
    CountDownLatch gate= new CountDownLatch(1);
    CountDownLatch end= new CountDownLatch(threadNum);
    for (int i = 0; i <threadNum ; i++) {
      new Thread(()->{
        try {
          gate.await();
          runnable.run();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          end.countDown();
        }
      }).start();
    }
    long beginTime = System.currentTimeMillis();
    gate.countDown();
    try {
      end.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }finally {
      long endTime = System.currentTimeMillis();
      System.out.println("所有线程执行结束时间"+(endTime-beginTime));
    }
  }

  static class MyRunnable implements Runnable {
    public volatile static Boolean isTrue = true;//公共资源
    public static Lock lock = new ReentrantLock();//如果不使用同步锁，多线程并发一定会出现并发安全问题  使用volatile关键字在高并发下不能完全保证线程安全

    @Override
    public void run() {
      try {
        String name = Thread.currentThread().getName();
        if (lock.tryLock()) {

          if (isTrue == true) {
            System.out.println("我是第一个抢到资源的线程，我是" + name);
            System.out.println("处理业务花费1ms");
            try {
              Thread.sleep(1);
              System.out.println("再次查看资源状态" + isTrue);
              if (isTrue == true) {
                System.out.println("资源没有被更改");
                isTrue = false;
              } else {
                System.out.println("资源已经被更改");
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          } else {
            System.out.println("资源已经被更改，我什么事也没做，我是线程" + name);
          }
        } else {
          System.out.println("我没有拿到锁，我是线程" + name);
        }
      } finally {
        //这里必须要先获取保证未释放才能释放锁   而且这里必须要释放锁
        if (lock.tryLock()) {
          lock.unlock();
        }
      }
    }
  }

  public static void main(String[] args) {
    MyRunnable myRunnable = new MyRunnable();
    concurrent(100,myRunnable);
  }
}