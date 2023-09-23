package com.fromscratch.users.infrastructure.httpserver;

import org.glassfish.jersey.server.ResourceConfig;

public interface IHttpServer {
    
    public static IHttpServer createDefaultHttpServer(int port, Class<? extends ResourceConfig> jerseyApplicationClass) {
        return new JettyHttpServer(port, jerseyApplicationClass);
    }
    
    void start();
    void stop();


}