import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyTimerTask {

   ArrayList a = new ArrayList<>();
   ArrayList b = new ArrayList<>();

  class MyTimerTask1 extends TimerTask {


    @Override
    public void run() {
      System.out.println("start：" + Thread.currentThread().getName());//获取当前系统时间
      try {
        for (int i = 0; i < 100; i++) {
         a.add(new data(i));
          Thread.sleep(500);
        }
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.out.println("stop：" + Thread.currentThread().getName());//获取当前系统时间
    }

  }
    class MyTimerTask2 extends TimerTask {



      @Override
      public void run() {
        System.out.println("start：" + Thread.currentThread().getName());//获取当前系统时间
        try {
          for (int i = 0; i < 100; i++) {
            b.add(new data(i));
            Thread.sleep(100);
          }
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        System.out.println("stop：" + Thread.currentThread().getName());//获取当前系统时间
      }
    }


    class data1 {

      ArrayList a = new ArrayList<>();
      data1 data1;



    }

     class data2 {

       ArrayList a = new ArrayList<>();
       data1 data1;

     }


  public  void test() {
    MyTimerTask1 task1 = new MyTimerTask1();
    MyTimerTask2 task2 = new MyTimerTask2();

//    ScheduledExecutorService executor = Executors.newScheduledThreadPool(4,
//        new ThreadFactoryBuilder().setNameFormat("DatanodeAdminMonitor-%d")
//            .setDaemon(false).build());
//
//    ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(4,
//        new ThreadFactoryBuilder().setNameFormat("DatanodeAdminMonitor-%d")
//            .setDaemon(false).build());

    ExecutorService executor1 = Executors.newSingleThreadExecutor();
    ExecutorService executor2 = Executors.newSingleThreadExecutor();
    //Executors提供的线程池ScheduledExecutorService中有两个方法，scheduleAtFixedRate 和 scheduleWithFixedDelay 。它们都可以延时且定期执行任务，但延时的时间是有差别的，下面介绍：
    //scheduleAtFixedRate ，中文意思为 以固定比率执行，参数有 Runnable command, long initialDelay,long period,TimeUnit unit第1次执行的时间是initialDelay(unit)，
    //第2次执行的时间是initialDelay+period(unit),第3次执行的时间是initialDelay+period*2(unit),依次类推。。。也就是，在任务开始后，period时间后，便会执行下一次任务。
    //如果period时间到了，但上一次还没执行完毕，则等待，直到上一次的任务执行完毕，然后马上执行本次任务。
    //scheduleWithFixedDelay ，中文意思为 以固定的延迟来执行，参数有Runnable command, long initialDelay,long delay,TimeUnit unit ，
    //该方法第1次执行也是在initialDelay(unit)后，但第2次执行是在第1次执行完毕后算起的delay时间后再执行。
    //因此，这两个方法的不同点是，scheduleAtFixedRate 是从任务开始时算起，scheduleWithFixedDelay 是从任务结束时算起。。
    //executor.scheduleAtFixedRate(task,3,3,TimeUnit.SECONDS);
//    executor2.scheduleWithFixedDelay(task2, 1, 1, TimeUnit.SECONDS);
    executor1.submit(task1);
    executor2.submit(task2);

  }

  class data {

    data(int i){
      a=i;
    }
    int a;
  }

  public static void main(String[] args) {
    MyTimerTask a = new MyTimerTask();
    for (int i=0;i<10000;i++){
      a.test();
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}