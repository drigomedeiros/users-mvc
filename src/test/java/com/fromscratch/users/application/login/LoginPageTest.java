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
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@DisplayName("Login Page")
@ExtendWith(HttpServerJUnitExtension.class)
class LoginPageTest {

    @Test
    @DisplayName("should send token on response header when login is successful")
    void shouldPresentHomePageWhenLoginIsSuccessful() {
        Form form = new Form();
        form.param("username", "1");
        form.param("password", "1");

        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "login");
        var response = webTarget.request().post(Entity.form(form));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getHeaderString(HttpHeaders.AUTHORIZATION)).isNotNull();
    }

    @Test
    @DisplayName("should return unauthorized response when login is not successful due to incorrect password")
    void shouldReturnUnauthorizedResponseWhenLoginIsNotSuccessfulDueToIncorrectPassword() {
        Form form = new Form();
        form.param("username", "admin@fromscratch.com");
        form.param("password", "admin12");

        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "login");
        var response = webTarget.request().post(Entity.form(form));
        assertThat(response.getHeaderString(HttpHeaders.AUTHORIZATION)).isNull();
        var responseMessage = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        assertThat(response.getHeaderString(HttpHeaders.AUTHORIZATION)).isNull();
        assertThat(responseMessage).isEqualTo("Incorrect login or password");
    }

    @Test
    @DisplayName("should return unauthorized response when login is not successful due to incorrect username")
    void shouldReturnUnauthorizedResponseWhenLoginIsNotSuccessfulDueToIncorrectUsername() {
        Form form = new Form();
        form.param("username", "admin12@fromscratch.com");
        form.param("password", "admin");

        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "login");
        var response = webTarget.request().post(Entity.form(form));
        var responseMessage = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        assertThat(response.getHeaderString(HttpHeaders.AUTHORIZATION)).isNull();
        assertThat(responseMessage).isEqualTo("Incorrect login or password");
        
    }

}
