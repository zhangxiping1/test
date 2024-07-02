import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MYLOG {
  public static final Logger LOG = LoggerFactory.getLogger(MYLOG.class);

  public static void printlog(){
    LOG.debug("MYLOG MYLOG MYLOG");
  }
}
