package com.fromscratch.users.application.users;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fromscratch.users.infrastructure.httpserver.HttpServerJUnitExtension;
import com.google.common.net.HttpHeaders;

import jakarta.ws.rs.client.ClientBuilder;

@DisplayName("Users Page")
@ExtendWith(HttpServerJUnitExtension.class)
class ListUsersPageTest {

    @Test
    @DisplayName("should present the users list when user is authenticated")
    void shouldPresentTheUsersListWhenUserIsAuthenticated() {
        var webTarget = ClientBuilder.newClient().target("http://localhost:9080/app/users");
        var response = webTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken").get().readEntity(String.class);

        assertThat(response).contains("Users", "Registered users", "<table", "<th>#ID</th>", "<th>Name</th>", "<th>Resource(s)</th>");
    }

    @Test
    @DisplayName("should redirect to unauthorized page when user is not authorized")
    void shouldRedirectToUnauthorizedPageWhenUserIsNotAuthorized() {
        var webTarget = ClientBuilder.newClient().target("http://localhost:9080/app/users");
        var response = webTarget.request().get().readEntity(String.class);

        assertThat(response).contains("You are not authorized to access this content");
    }
    
}
