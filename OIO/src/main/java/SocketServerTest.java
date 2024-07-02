import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class SocketServerTest
{
  public static final int PORT = 8000;
  public static final int BACKLOG = 2;
  public static void main( String[] args ) throws IOException
  {
    ServerSocket server = new ServerSocket(PORT);
    System.out.println("started: " + server);
    try
    {
      Socket socket = server.accept();
      try
      {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String info = null;

        while( true )
        {
          info = in.readLine();
          System.out.println( "server received --- " + info );
        }
      }
      finally
      {
        socket.close();
      }
    }
    finally
    {
      server.close();
    }
  }
}
