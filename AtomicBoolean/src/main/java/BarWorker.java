/**
 * if (!exists) {
 * exists = true;   判断和赋值一起构成原子操作  （如果中间有其他的代码，就构不成原子操作）
 *
 * 作用  private static boolean exists = false;  线程共享变量   同一时间 BarWorker 10 个线程 只用一个线程在运行
 *
 *
  */
class BarWorker implements Runnable {

    private static boolean exists = false;

    private String name;

    public BarWorker(String name) {
        this.name = name;
    }

    public void run() {
        if (!exists) {
            exists = true;
            System.out.println(name + " enter");
            System.out.println(name + " working");
            System.out.println(name + " leave");
            exists = false;
        } else {
            System.out.println(name + " give up");
        }
    }

    public static void main(String[] args) {

    }

}