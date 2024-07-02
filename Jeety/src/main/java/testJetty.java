import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 *
 * -Xms256m -Xmx512m -XX:PermSize=256M -XX:MaxPermSize=256M
 *
 * @see <url>http://docs.codehaus.org/display/JETTY/Embedding+Jetty</url>
 *      OneWebApp.java
 *
 *      Using embedded mode for OneWebApp
 *
 *      <p>
 *      dependencies: jasper-compiler-5.5.15.jar jasper-compiler-jdt-5.5.15.jar
 *      jasper-runtime-5.5.15.jar
 *
 *      jetty-6.1.7.jar jetty-util-6.1.7.jar
 *
 *      jsp-api-2.0.jar servlet-api-2.5-6.1.7.jar
 *
 *      slf4j-api-1.3.1.jar slf4j-simple-1.3.1.jar
 *
 *      xercesImpl-2.6.2.jar xmlParserAPIs-2.6.2.jar
 *
 *
 *
 */
public class testJetty  {
  //这个文件在类路径下面
  public static final String DB_RESOURCE_PATH = "applicationContext.properties";

  private int port = 8000;
  private String host = "127.0.0.1";
  private String propertyPath = DB_RESOURCE_PATH;
  private String contextPath = "/WebContent";

  private String warApp = "WebContent";

  /**
   *
   * @param propertyPath
   * @return
   */
  public testJetty propertyPath(String propertyPath) {
    this.propertyPath = propertyPath;
    return this;
  }

  /**
   * @param port
   *            the port to set
   */
  public testJetty port(int port) {
    this.port = port;
    return this;
  }

  /**
   * @param host
   *            the host to set
   */
  public testJetty host(String host) {
    this.host = host;
    return this;
  }

  /**
   * @param warApp
   *            the warApp to set
   */
  public testJetty warApp(String warApp) {
    this.warApp = warApp;
    return this;
  }
  //Jetty调用的主方法
  public void start() throws Exception {

    long begin = System.nanoTime();// .currentTimeMillis();
    //创建Jetty服务器
    Server server = new Server();
    //为Jetty服务器配置属性
    Properties properties = new Properties();
    InputStream input =new BufferedInputStream(new FileInputStream(propertyPath));
    properties.load(input);
    port = Integer.parseInt(properties.getProperty("http.port"));
    contextPath = properties.getProperty("http.context.path");
    //设置连接
    Connector connector = new SelectChannelConnector();
    connector.setPort(Integer.getInteger("jetty.port", port).intValue());
    server.setConnectors(new Connector[] { connector });
    WebAppContext webAppContext = new WebAppContext(warApp, contextPath);
    //找到对应的Jetty配置文件 这个文件不需要修改
    webAppContext.setDefaultsDescriptor("./runtime/webdefault.xml");
    server.setHandler(webAppContext);

    host = (null == connector.getHost() ? host : connector.getHost());

    try {
      server.start();
      String url = "http://" + host + ":" + port + contextPath;
      System.out.println("[Jetty Server started in " + (System.nanoTime() - begin) / 1000 / 1000 / 1000 + "s]: " + url);
      //server.join();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(100);
    }

  }

  public static void main(String[] args) throws Exception {
    new testJetty().start();
  }
}

