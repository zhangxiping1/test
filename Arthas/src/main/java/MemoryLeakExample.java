import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryLeakExample {
  private List<DDD> list = new ArrayList<>();

  public void addData1(AAA data) {
    list.add(data);
  }

  public void addData2(BBB data) {
    list.add(data);
  }

  public static void main(String[] args) throws InterruptedException {
    MemoryLeakExample example1 = new MemoryLeakExample();
    MemoryLeakExample example2 = new MemoryLeakExample();
    for (int i = 0; i < 1000000; i++) {
      Thread.sleep(1);
      example1.addData1(new AAA(new CCC("name")));
      example2.addData2(new BBB(new CCC("name")));
      if(i%10000 == 0) {
        System.out.println("i = " + i);
      }
    }
  }


  static class AAA implements DDD{
    CCC name ;
    HashMap<String, String> map = new HashMap<>();

    public AAA(CCC s) {
      name = s;
      map.put("key", "value");
    }
  }

  static class BBB implements DDD{
    CCC name ;

    public BBB(CCC s) {
      name = s;
    }
  }

  static class CCC {
    String name ;

    public CCC(String s) {
      name = s;
    }
  }

  interface  DDD {

  }
}
