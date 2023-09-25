package com.fromscratch.users.application.home;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fromscratch.users.application.ApplicationTestUtils;
import com.fromscratch.users.infrastructure.httpserver.HttpServerJUnitExtension;
import com.google.common.net.HttpHeaders;

import jakarta.ws.rs.client.ClientBuilder;

@DisplayName("Home Page")
@ExtendWith(HttpServerJUnitExtension.class)
class HomePageTest {

    @Test
    @DisplayName("should present the spa when user is authenticated")
    void shouldPresentSinglePageApplicationWhenUserIsAuthenticated() {
        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI);
        var response = webTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken").get().readEntity(String.class);

        assertThat(response)
                    .contains("<h5>Welcome to Users MVC page.</h5>")
                    .contains("<a href=\"#\" class=\"d-block\" id=\"userName\">John</a>")
                    .contains("<a href=\"#\" class=\"nav-link simpleNavigation\" simple-target=\"/app/users\">")
                    .contains("<a href=\"#\" class=\"nav-link simpleNavigation\" simple-target=\"/app/initial\">");
    }

    @Test
    @DisplayName("should return initial content")
    void shouldReturnInitialContent() {
        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "initial");
        var response = webTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken").get().readEntity(String.class);

        assertThat(response)
            .contains("Begin")
            .contains("Welcome to Users MVC page")
            .contains("This page was created to demonstrate the use of Jakarta MVC specification with Jersey");
    }

    @Test
    @DisplayName("should redirect to unauthorized page when user is not authorized")
    void shouldRedirectToUnauthorizedPageWhenUserIsNotAuthorized() {
        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "initial");
        var response = webTarget.request().get().readEntity(String.class);

        assertThat(response).contains("You're not authorized to access this content");
    }
    
}
