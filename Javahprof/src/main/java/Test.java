import java.util.ArrayList;
import java.util.List;

public class Test {
  aaa aaa = new aaa();
  aaa bbb = new aaa();
  public static void main(String[] args) throws InterruptedException {
    Test test = new Test();
    while (true){
      System.out.println(test.memoryTest(1));
      Thread.sleep(5000);
    }
  }
  public String memoryTest(int c) {
    byte[] a = new byte[c * 1024 * 1024];
    byte[] b = new byte[c * 1024 * 1024];
    aaa.add(a);
    bbb.add(b);
    return "success ";
  }

  class  aaa {
    List<byte[]> memoryList1 = new ArrayList<>();
    void add(byte[] a){
      memoryList1.add(a);
    }
  }
}
