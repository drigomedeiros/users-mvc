package com.fromscratch.users.application.unauthorized;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fromscratch.users.application.ApplicationTestUtils;
import com.fromscratch.users.infrastructure.httpserver.HttpServerJUnitExtension;

import jakarta.ws.rs.client.ClientBuilder;

@DisplayName("Unauthorized Page")
@ExtendWith(HttpServerJUnitExtension.class)
class UnauthorizedPageTest {
    
    @Test
    @DisplayName("should display a message when user is not authenticated")
    void shouldDisplayAMessageWhenUserIsNotAuthenticated() {
        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "unauthorized");
        var response = webTarget.request().get().readEntity(String.class);

        assertThat(response)
            .contains("You are not authorized to access this content");
    }

}
