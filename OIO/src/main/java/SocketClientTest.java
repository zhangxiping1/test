
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class SocketClientTest {
  public static final int PORT = 8000;

  public static void main( String[] args ) throws Exception
  {
    SocketAddress addr = new InetSocketAddress(InetAddress.getLocalHost(), PORT);
    Socket socket = new Socket();

    try
    {
//      WritableByteChannel channel = socket.getChannel();
      SocketChannel channel = socket.getChannel();
      socket.connect(addr, 3000);
      socket.setSendBufferSize(16*1024);
      socket.setSoTimeout(30000);

      Selector selector = Selector.open();
      DataOutputStream out = new DataOutputStream(new BufferedOutputStream( socket.getOutputStream()));
      BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream()) );
      int i = 0;


      System.out.println("client sent --- hello *** " + i++);
//        out.writeBytes( "client sent --- hello ***\n");
//        out.flush();
      channel.write(ByteBuffer.wrap("client sent --- hello *** ".getBytes()));
      int count = 0;
      try {
        count = selector.select(30000);
      } catch (IOException e) { //unexpected IOException.
        throw e;
      }

      if (count == 0) {
        throw new SocketTimeoutException();
      }
    }
    finally
    {
      socket.close();
    }
  }
}
