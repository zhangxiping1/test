import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedFrame;
import jdk.jfr.consumer.RecordedMethod;
import jdk.jfr.consumer.RecordedStackTrace;
import jdk.jfr.consumer.RecordingFile;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Testhprof {

  @Test
  public  void Testhprof() {

    String args ="D:\\project\\myproject\\JavaLearn\\heap.hprof";
    try (RecordingFile f = new RecordingFile(Paths.get(args))) {
      Map<String, AbstractMap.SimpleEntry<String, Integer>> histogram = new HashMap<>();
      int total = 0;
      while (f.hasMoreEvents()) {
        RecordedEvent event = f.readEvent();
        if (event.getEventType().getName().equals("jdk.ExecutionSample")) {
          RecordedStackTrace s = event.getStackTrace();
          if (s != null) {
            RecordedFrame topFrame= s.getFrames().get(0);
            if (topFrame.isJavaFrame())  {
              RecordedMethod method = topFrame.getMethod();
              String methodName = method.getType().getName() + "#" + method.getName() + " " + method.getDescriptor();
              Map.Entry entry = histogram.computeIfAbsent(methodName, u -> new AbstractMap.SimpleEntry<String, Integer>(methodName, 0));
//              entry.setValue(entry.getValue() + 1);
              total++;
            }
          }
        }
      }
      List<AbstractMap.SimpleEntry<String, Integer>> entries = new ArrayList<>(histogram.values());
      entries.sort((u, v) -> v.getValue().compareTo(u.getValue()));
      for (AbstractMap.SimpleEntry<String, Integer> c : entries) {
        System.out.printf("%2.0f%% %s\n", 100 * (float) c.getValue() / total, c.getKey());
      }
      System.out.println("\nSample count: " + total);
    } catch (IOException ioe) {
      System.out.println("Error reading file " + args + ". " + ioe.getMessage());
    }
  }
}
