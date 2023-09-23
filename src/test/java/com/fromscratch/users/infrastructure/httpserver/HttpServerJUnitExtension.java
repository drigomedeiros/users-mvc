package com.fromscratch.users.infrastructure.httpserver;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.fromscratch.users.infrastructure.jakartaee.JakartaApp;

public class HttpServerJUnitExtension implements BeforeAllCallback, AfterAllCallback {

    IHttpServer server = IHttpServer.createDefaultHttpServer(9080, JakartaApp.class);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        server.start();
    }
    
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        server.stop();
    }
    
}
