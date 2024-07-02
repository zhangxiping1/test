import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class TestLinkBlockQueueBUG {
  public  LinkedBlockingQueue<ApplicationEvent> eventQueue = new LinkedBlockingQueue<>();
  public enum ApplicationEventType {
    INIT_APPLICATION,
    INIT_CONTAINER,
    FINISH_APPLICATION
  }

  public static class ApplicationEvent  {
    private final String applicationID;
    private final ApplicationEventType eventType;
    public ApplicationEvent(String appID,ApplicationEventType eventType) {
      this.applicationID = appID;
      this.eventType = eventType;
    }
    public ApplicationEventType getType() {
      return eventType;
    }
  }

  private final class handler extends Thread {

    public handler(int index){
      super("handle "+ index);
    }
    @Override
    public void run() {
      for (int i = 0; i < 100000; i++) {
        try {
          //模拟其他 handler 生成,消费 application事件
          eventQueue.put(new ApplicationEvent("application_1684828368775_103463",ApplicationEventType.INIT_APPLICATION));
          eventQueue.put(new ApplicationEvent("application_1684828368775_103463",ApplicationEventType.INIT_CONTAINER));
          eventQueue.put(new ApplicationEvent("application_1684828368775_103463",ApplicationEventType.FINISH_APPLICATION));
          eventQueue.take();
//          Thread.sleep(5);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  @Test
  public void TestLinkBlockQueueBUGWithIterator() {

    Thread.currentThread().setName("printEventQueueDetails");
    for (int i = 0; i < 10; i++) {
      new handler(i).start();
    }
    while (true) {
      //模拟打印队列细节
      int size = eventQueue.size();
      if (size != 0 && size > 1000 ) {
        System.out.println("begin  printEventQueueDetails ,size ="+size);
        Iterator<ApplicationEvent> iterator = eventQueue.iterator();
        Map<Enum, Long> counterMap = new HashMap<>();
        while (iterator.hasNext()) {
          Enum eventType = iterator.next().getType();
          if (!counterMap.containsKey(eventType)) {
            counterMap.put(eventType, 0L);
          }
          counterMap.put(eventType, counterMap.get(eventType) + 1);
        }
        for (Map.Entry<Enum, Long> entry : counterMap.entrySet()) {
          long num = entry.getValue();
          System.out.println("Event type: " + entry.getKey()
              + ", Event record counter: " + num);
        }

        System.out.println("printEventQueueDetails end");
        System.exit(0);
      }
    }
  }


  @Test
  public void TestLinkBlockQueueBUGWithStream() {

    Thread.currentThread().setName("printEventQueueDetails");
    for (int i = 0; i < 10; i++) {
      new handler(i).start();
    }

    while (true) {
      //模拟打印队列细节
      int size = eventQueue.size();
      if (size != 0 && size > 1000 ) {
        System.out.println("begin  printEventQueueDetails ,size ="+size);
        Map<Enum, Long> counterMap = eventQueue
            .stream()
            .collect(Collectors.groupingBy(e -> e.getType(), Collectors.counting())
            );
        for (Map.Entry<Enum, Long> entry : counterMap.entrySet()) {
          long num = entry.getValue();
          System.out.println("Event type: " + entry.getKey()
              + ", Event record counter: " + num);
        }
        System.out.println("printEventQueueDetails end");
        System.exit(0);
      }
    }
  }
}
