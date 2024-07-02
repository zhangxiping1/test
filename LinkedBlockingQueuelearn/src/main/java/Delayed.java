import java.util.concurrent.TimeUnit;

public interface Delayed {
  public long getDelay(TimeUnit unit) ;
  public int compareTo(Delayed o) ;
}
