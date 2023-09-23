package com.fromscratch.users.application.login;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fromscratch.users.application.ApplicationTestUtils;
import com.fromscratch.users.infrastructure.httpserver.HttpServerJUnitExtension;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;

@DisplayName("Login Page")
@ExtendWith(HttpServerJUnitExtension.class)
class LoginPageTest {

    @Test
    @DisplayName("should present an authentication form")
    void shouldPresentAnAuthenticationForm() {
        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI);
        var response = webTarget.request().get().readEntity(String.class);

        assertThat(response)
            .contains("<a href=\"javascript: void(0);\" class=\"h1\"><b>Users</b>MVC</a>")
            .doesNotContain("Incorrect login or password");
    }

    @Test
    @DisplayName("should present authentication form with error if unauthorized query param is present")
    void shouldPresentAnAuthenticationFormWithErrorIfUnauthorizedQueryParamIsPresent() {
        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "?unauthorized=true");
        var response = webTarget.request().get().readEntity(String.class);

        assertThat(response)
            .contains("<a href=\"javascript: void(0);\" class=\"h1\"><b>Users</b>MVC</a>")
            .contains("You are not authorized to access this page");
        
    }

    @Test
    @DisplayName("should present home page after successful login")
    void shouldPresentHomePageWhenLoginIsSuccessful() {
        Form form = new Form();
        form.param("username", "1");
        form.param("password", "1");

        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI);
        var response = webTarget.request().post(Entity.form(form)).readEntity(String.class);

        assertThat(response)
                    .contains("<h5>Welcome to Users MVC page.</h5>")
                    .contains("<a href=\"#\" class=\"d-block\" id=\"userName\">John</a>")
                    .contains("<a href=\"/app/users\" class=\"nav-link spaNavigation\">")
                    .contains("<a href=\"/app/home/initial\" class=\"nav-link active spaNavigation\">");
    }

    @Test
    @DisplayName("should return unauthorized response when login is not successful due to incorrect password")
    void shouldReturnUnauthorizedResponseWhenLoginIsNotSuccessfulDueToIncorrectPassword() {
        Form form = new Form();
        form.param("username", "admin@fromscratch.com");
        form.param("password", "admin12");

        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI);
        var response = webTarget.request().post(Entity.form(form));
        var responseMessage = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        assertThat(responseMessage).isEqualTo("Incorrect login or password");
    }

    @Test
    @DisplayName("should return unauthorized response when login is not successful due to incorrect username")
    void shouldReturnUnauthorizedResponseWhenLoginIsNotSuccessfulDueToIncorrectUsername() {
        Form form = new Form();
        form.param("username", "admin12@fromscratch.com");
        form.param("password", "admin");

        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI);
        var response = webTarget.request().post(Entity.form(form));
        var responseMessage = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        assertThat(responseMessage).isEqualTo("Incorrect login or password");
        
    }

}
