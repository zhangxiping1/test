package org.apache.hadoop.ipc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class server {
  public static final Logger LOG =
      Logger.getLogger("org.apache.hadoop.ipc.Server");
  private int port;
  private int timeout;
  private boolean running = true;
  private Class paramClass;                       // class of call parameters
  private int handlerCount;                       // number of handler threads
  private int maxQueuedCalls;                     // max number of queued calls
  private LinkedList callQueue = new LinkedList(); // queued calls

  private Object callDequeued = new Object();     // used by wait/notify

  private class Listener extends Thread {
    private ServerSocket socket;

    public Listener() throws IOException {
      this.socket = new ServerSocket(port);
      socket.setSoTimeout(timeout);
      this.setDaemon(true);
      this.setName("Server listener on port " + port);
    }

    public void run() {
      LOG.info(getName() + ": starting");
      while (running) {
        try {
          new Connection(socket.accept()).start(); // start a new connection
        } catch (SocketTimeoutException e) {      // ignore timeouts
        } catch (Exception e) {                   // log all other exceptions
          LOG.log(Level.INFO, getName() + " caught: " + e, e);
        }
      }
      try {
        socket.close();
      } catch (IOException e) {
        LOG.info(getName() + ": e=" + e);
      }
      LOG.info(getName() + ": exiting");
    }
  }

  private static class Call {
    private int id;                               // the client's call id
    private Writable param;                       // the parameter passed
    private Connection connection;                // connection to client

    public Call(int id, Writable param, Connection connection) {
      this.id = id;
      this.param = param;
      this.connection = connection;
    }
  }

  private class Connection extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Connection(Socket socket) throws IOException {
      this.socket = socket;
      socket.setSoTimeout(timeout);
      this.in = new DataInputStream
          (new BufferedInputStream(socket.getInputStream()));
      this.out = new DataOutputStream
          (new BufferedOutputStream(socket.getOutputStream()));
      this.setDaemon(true);
      this.setName("Server connection on port " + port + " from "
          + socket.getInetAddress().getHostAddress());
    }

    public void run() {
      LOG.info(getName() + ": starting");
      try {
        while (running) {
          int id;
          try {
            id = in.readInt();                    // try to read an id
          } catch (SocketTimeoutException e) {
            continue;
          }

          if (LOG.isLoggable(Level.FINE))
            LOG.fine(getName() + " got #" + id);

          Writable param = makeParam();           // read param
          param.readFields(in);

          Call call = new Call(id, param, this);

          synchronized (callQueue) {
            callQueue.addLast(call);              // queue the call
            callQueue.notify();                   // wake up a waiting handler
          }

          while (running && callQueue.size() >= maxQueuedCalls) {
            synchronized (callDequeued) {         // queue is full
              callDequeued.wait(timeout);         // wait for a dequeue
            }
          }
        }
      } catch (EOFException eof) {
        // This is what happens on linux when the other side shuts down
      } catch (SocketException eof) {
        // This is what happens on Win32 when the other side shuts down
      } catch (Exception e) {
        LOG.log(Level.INFO, getName() + " caught: " + e, e);
      } finally {
        try {
          socket.close();
        } catch (IOException e) {}
        LOG.info(getName() + ": exiting");
      }
    }

  }

  private class Handler extends Thread {
    public Handler(int instanceNumber) {
      this.setDaemon(true);
      this.setName("Server handler "+ instanceNumber + " on " + port);
    }

    public void run() {
      LOG.info(getName() + ": starting");
      while (running) {
        try {
          Call call;
          synchronized (callQueue) {
            while (running && callQueue.size()==0) { // wait for a call
              callQueue.wait(timeout);
            }
            if (!running) break;
            call = (Call)callQueue.removeFirst(); // pop the queue
          }

          synchronized (callDequeued) {           // tell others we've dequeued
            callDequeued.notify();
          }

          if (LOG.isLoggable(Level.FINE))
            LOG.fine(getName() + ": has #" + call.id + " from " +
                call.connection.socket.getInetAddress().getHostAddress());

          String error = null;
          Writable value = null;
          try {
            value = call(call.param);             // make the call
          } catch (IOException e) {
            LOG.log(Level.INFO, getName() + " call error: " + e, e);
            error = e.getMessage();
          } catch (Exception e) {
            LOG.log(Level.INFO, getName() + " call error: " + e, e);
            error = e.toString();
          }

          DataOutputStream out = call.connection.out;
          synchronized (out) {
            out.writeInt(call.id);                // write call id
            out.writeBoolean(error!=null);        // write error flag
//            if (error != null)
//              value = new UTF8(error);
            value.write(out);                     // write value
            out.flush();
          }

        } catch (Exception e) {
          LOG.log(Level.INFO, getName() + " caught: " + e, e);
        }
      }
      LOG.info(getName() + ": exiting");
    }
  }

  /** Constructs a server listening on the named port.  Parameters passed must
   * be of the named class.  The <code>handlerCount</handlerCount> determines
   * the number of handler threads that will be used to process calls.
   */
  protected server(int port, Class paramClass, int handlerCount, Configuration conf) {
    this.port = port;
    this.paramClass = paramClass;
    this.handlerCount = handlerCount;
    this.maxQueuedCalls = handlerCount;
    this.timeout = conf.getInt("ipc.client.timeout",10000);
  }

  /** Sets the timeout used for network i/o. */
  public void setTimeout(int timeout) { this.timeout = timeout; }

  /** Starts the service.  Must be called before any calls will be handled. */
  public synchronized void start() throws IOException {
    Listener listener = new Listener();
    listener.start();

    for (int i = 0; i < handlerCount; i++) {
      Handler handler = new Handler(i);
      handler.start();
    }
  }

  public abstract Writable call(Writable param) throws IOException;

  private Writable makeParam() {
    Writable param;                               // construct param
    try {
      param = (Writable)paramClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e.toString());
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e.toString());
    }
    return param;
  }


}
