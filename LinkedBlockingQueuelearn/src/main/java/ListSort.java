import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListSort {
  public static void main(String[] args) throws InterruptedException {

    List list = new ArrayList();

    list.add(new apple("a"));

    list.add(new apple("b"));

    list.add(new apple("c"));

    list.add(null);

    List sortedQueue = new ArrayList<>(list);

    new Thread(new Runnable() {
      @Override
      public void run() {
        int i = 3;
        while (true) {
          ((apple)list.get(i%3)).setName("d");
          i=i++/10000;
        }
      }
    }).start();

    Collections.sort(sortedQueue);
    Collections.sort(sortedQueue);
    Thread.sleep(1000);
    Collections.sort(sortedQueue);
    Thread.sleep(1000);
    Collections.sort(sortedQueue);
    Thread.sleep(1000);
    Collections.sort(sortedQueue);

    System.out.println(sortedQueue.toString());
  }


  static class apple implements Comparable {
    private String name;

    apple(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "apple{" +
          "name='" + name + '\'' +
          '}';
    }

    public String getName(){
      return this.name;
    }

    public void setName(String name){
     this.name = name;
    }

    @Override
    public int compareTo(Object o) {
      if (o instanceof apple) {
        apple p = (apple) o;
        if (this.getName().compareTo(p.getName()) > 0) {
          return 1;
        } else if (this.getName().compareTo(p.getName()) < 0) {
          return -1;
        } else {
          return 0;
        }
      }
      return 0;
    }
  }
}