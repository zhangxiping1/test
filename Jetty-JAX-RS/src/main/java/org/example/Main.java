package org.example;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.Servlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {

  public static void main(String[] args) {

    Server server = new Server(8080);

    ServletContextHandler ctx =
        new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

    ctx.setContextPath("/");
    server.setHandler(ctx);

    ServletHolder serHol = ctx.addServlet((Class<? extends Servlet>) ServletContainer.class, "/rest/*");
    serHol.setInitOrder(1);
    serHol.setInitParameter("jersey.config.server.provider.packages",
        "org.res");

    try {
      server.start();
      server.join();
    } catch (Exception ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    } finally {

      server.destroy();
    }
  }
}
