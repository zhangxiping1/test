//import org.junit.Test;
//
public class myThreadpoolTest {

//    @Test
//    public void execute() {
//        myThreadpool myThreadpool = new myThreadpool();
//        myThreadpool.execute(new Runnable() {
//        @Override
//        public void run() {
//            System.out.println("test1");
//        }
//    });
//
//        for (int i=0;i<10;i++){
//        myThreadpool.execute(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("test");
//            }
//        });
//
//    }
//    }

    public static void main(String[] args) {
        myThreadpool myThreadpool = new myThreadpool();

        for (int i=0;i<10;i++){
            myThreadpool.execute(new Runnable() {
                @Override
                public void run() {
                    pr();
                }
            });

        }
    }


    static void pr(){
        int i=0;
        for (i=1;i<20;i++){
            System.out.println(Thread.currentThread().getName()+"str"+i);
        }

    }
}