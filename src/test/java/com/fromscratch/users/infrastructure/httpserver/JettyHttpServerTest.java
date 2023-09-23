package com.fromscratch.users.infrastructure.httpserver;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

import com.fromscratch.users.infrastructure.jakartaee.JakartaApp;

class JettyHttpServerTest {

    @Test
    void shouldRunAHttpServer() {
        IHttpServer httpServer = IHttpServer.createDefaultHttpServer(9080, JakartaApp.class);

        assertThatCode(() -> httpServer.start()).doesNotThrowAnyException();
        assertThatCode(() -> httpServer.stop()).doesNotThrowAnyException();
    }
    
}
