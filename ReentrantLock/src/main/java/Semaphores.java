import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Semaphores {

  static int a =0;

  static class TaskDemo implements Runnable{
    private String id;
    TaskDemo(String id){
      this.id = id;
    }
    @Override
    public void run(){
      try {
        Random rnd = new Random();
        System.out.println("Thread " + id + " is working");
        a++;
        System.out.println("*************** a="+ a);
        System.out.println("Thread " + id + " is over");
      } catch (Exception e) {
      }
    }
  }

  public static void main(String[] args){
    //注意我创建的线程池类型，
    ExecutorService se = Executors.newCachedThreadPool();
    se.submit(new TaskDemo("a"));
    se.submit(new TaskDemo("b"));
    se.submit(new TaskDemo("c"));
    se.submit(new TaskDemo("d"));
    se.submit(new TaskDemo("e"));
    se.submit(new TaskDemo("f"));
    se.submit(new TaskDemo("g"));
    se.submit(new TaskDemo("h"));
    se.submit(new TaskDemo("i"));
    se.shutdown();
  }
}

