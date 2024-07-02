import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class UseReentrantReadWriteLock {
    //读写锁对象
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    //获得读锁
    private ReadLock readLock = rwLock.readLock();
    //获得写锁
    private WriteLock writeLock = rwLock.writeLock();

    public void read() {
        readLock.lock();
        try {
            System.err.println("线程 " + Thread.currentThread().getName() + " 进入了读方法。。。");
            Thread.sleep(3000);
            System.err.println("线程 " + Thread.currentThread().getName() + " 退出了读方法。。。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void write() {
        writeLock.lock();
        try {
            System.err.println("线程 " + Thread.currentThread().getName() + " 进入了写方法。。。");
            Thread.sleep(3000);
            System.err.println("线程 " + Thread.currentThread().getName() + " 执行了写方法。。。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        UseReentrantReadWriteLock urwLock = new UseReentrantReadWriteLock();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                urwLock.read();
            }
        }, "t1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                urwLock.read();
            }
        }, "t2");

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                urwLock.write();
            }
        }, "t3");



        t1.start();
        t2.start();
        t3.start();


    }
}
