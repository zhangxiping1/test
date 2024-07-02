import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionUseCase {
    // 线程间通过操作同一个锁的信号量  来相互影响
    public Lock lock = new ReentrantLock();
    //通过这把锁，拿到一个信号量
    public Condition condition = lock.newCondition();

    public static void main(String[] args)  {
        ConditionUseCase useCase = new ConditionUseCase();
        ExecutorService executorService = Executors.newFixedThreadPool (2);


        executorService.execute(new Runnable() {
            @Override
            public void run() {
                useCase.conditionWait();
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                useCase.conditionSignal();
            }
        });

        executorService.shutdown();
    }

    public void conditionWait()  {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "拿到锁了");
            System.out.println(Thread.currentThread().getName() + "等待信号");
            //通过信号量主动释放锁   等待其他线程执行完
            condition.await();
            System.out.println(Thread.currentThread().getName() + "拿到信号");
            System.out.println(Thread.currentThread().getName() + "又等待信号");
            condition.signal();
            System.out.println(Thread.currentThread().getName() + "发出信号");
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }
    public void conditionSignal() {
        lock.lock();
        try {
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName() + "拿到锁了");
            condition.signal(); //唤醒等待的线程，如果在wait之前没有调用，都会阻塞
            System.out.println(Thread.currentThread().getName() + "发出信号");
            condition.await();
            System.out.println(Thread.currentThread().getName() + "拿到信号");
        }catch (Exception e){

        }finally {
            lock.unlock();
        }
    }

}