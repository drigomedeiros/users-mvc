package com.fromscratch.users.infrastructure.httpserver;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


public class JettyHttpServer implements IHttpServer{

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Server server;
    private int port;
    private Class<? extends ResourceConfig> applicationClass;

    public JettyHttpServer(int port, Class<? extends ResourceConfig> applicationClass) {
        this.port = port;
        this.applicationClass = applicationClass;
    }

    @Override
    public void start() {
        try {

            logger.info("Starting server...");

            server = new Server(port);

            String staticContent = new File("src/main/resources/webstatic").getAbsolutePath();
            
            ServletContextHandler mainContext = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
            mainContext.setContextPath("/");
            server.setHandler(mainContext);

            var jerseyHolder = new ServletHolder(new ServletContainer(getApplication()));
            mainContext.addServlet(jerseyHolder, "/app/*");

            var staticContentHolder = new ServletHolder("default", DefaultServlet.class);
            staticContentHolder.setInitParameter("resourceBase", staticContent);
            staticContentHolder.setInitParameter("dirAllowed", "false");
            staticContentHolder.setInitParameter("pathInfoOnly", "true");
            mainContext.addServlet(staticContentHolder, "/*");
 
            server.start();
            
        } catch (Exception e) {

            logger.log(Level.SEVERE, "Jetty server failed to start", e);

        }
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private ResourceConfig getApplication()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Object[] args = new Object[0];
        return (ResourceConfig) applicationClass.getConstructors()[0].newInstance(args);
    }
    
}
