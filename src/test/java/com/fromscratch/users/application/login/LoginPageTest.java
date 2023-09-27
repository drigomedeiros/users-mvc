package com.fromscratch.users.application.login;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fromscratch.users.application.ApplicationTestUtils;
import com.fromscratch.users.domain.authentication.AuthenticationRequest;
import com.fromscratch.users.infrastructure.httpserver.HttpServerJUnitExtension;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@DisplayName("Login Page")
@ExtendWith(HttpServerJUnitExtension.class)
class LoginPageTest {

    @Test
    @DisplayName("should send token on response header when login is successful")
    void shouldPresentHomePageWhenLoginIsSuccessful() {
        var authenticationRequest = new AuthenticationRequest("1", "1");
        
        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "login");
        var response = webTarget.request().post(Entity.json(authenticationRequest));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getHeaderString(HttpHeaders.AUTHORIZATION)).isNotNull();
    }

    @Test
    @DisplayName("should return unauthorized response when login is not successful due to incorrect password")
    void shouldReturnUnauthorizedResponseWhenLoginIsNotSuccessfulDueToIncorrectPassword() {
        var authenticationRequest = new AuthenticationRequest("1", "2");

        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "login");
        var response = webTarget.request().post(Entity.json(authenticationRequest));
        assertThat(response.getHeaderString(HttpHeaders.AUTHORIZATION)).isNull();
        var responseMessage = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        assertThat(response.getHeaderString(HttpHeaders.AUTHORIZATION)).isNull();
        assertThat(responseMessage).isEqualTo("Incorrect login or password");
    }

    @Test
    @DisplayName("should return unauthorized response when login is not successful due to incorrect username")
    void shouldReturnUnauthorizedResponseWhenLoginIsNotSuccessfulDueToIncorrectUsername() {
        var authenticationRequest = new AuthenticationRequest("2", "1");

        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "login");
        var response = webTarget.request().post(Entity.json(authenticationRequest));
        var responseMessage = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        assertThat(response.getHeaderString(HttpHeaders.AUTHORIZATION)).isNull();
        assertThat(responseMessage).isEqualTo("Incorrect login or password");
        
    }

}
