package com.fromscratch.users.infrastructure.jakartaee;

import java.util.logging.Logger;

import com.fromscratch.users.infrastructure.httpserver.IHttpServer;

public class JakartaMvcBoot {

    private JakartaMvcBoot() {}

    private static final Logger logger = Logger.getLogger(JakartaMvcBoot.class.getName());
    private static final IHttpServer server = IHttpServer.createDefaultHttpServer(8080, JakartaApp.class);

    public static void run() {
        server.start();
        logger.info("Server started at port 8080");
        Runtime.getRuntime().addShutdownHook(new Thread(JakartaMvcBoot::stop));
    }

    public static void stop(){
        server.stop();
    }
    
}
