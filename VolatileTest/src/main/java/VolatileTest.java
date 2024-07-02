public class VolatileTest extends Thread {

    boolean flag = false;
    int i = 0;
    int j=0;
    public void run() {
        while (!flag) {
            i++;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println( 0 >= 0);
//        VolatileTest vt = new VolatileTest();
//        vt.start();
//        Thread.sleep(10);
//        vt.flag = true;
//        System.out.println("stope" + vt.i);
    }
}
