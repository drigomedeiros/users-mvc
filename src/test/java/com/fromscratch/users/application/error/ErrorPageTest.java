package com.fromscratch.users.application.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fromscratch.users.application.ApplicationTestUtils;
import com.fromscratch.users.infrastructure.httpserver.HttpServerJUnitExtension;

import jakarta.ws.rs.client.ClientBuilder;

@DisplayName("Error Page")
@ExtendWith(HttpServerJUnitExtension.class)
class ErrorPageTest {
    
    @Test
    @DisplayName("should display a message according to the error code")
    void shouldDisplayMessageAccordingToErrorCode() {
        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "error?code=401");
        var response = webTarget.request().get().readEntity(String.class);

        assertThat(response)
            .contains("You are not authorized to access this content");

        webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "error?code=404");
        response = webTarget.request().get().readEntity(String.class);

        assertThat(response)
            .contains("You are trying to access a resource that does not exist.");
        
        webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "error?code=424");
        response = webTarget.request().get().readEntity(String.class);

        assertThat(response)
            .contains("Ooops! Some of our external resources we rely on are not available. Wait and try again.");

        webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "error?code=503");
        response = webTarget.request().get().readEntity(String.class);

        assertThat(response)
            .contains("Ooops! Server is not available. Trying to fix it.");
    }

    @Test
    @DisplayName("should display a good song when we don't know what had happened")
    void shouldDisplayAGoodSongWhenWeDoNotKnowWhatHadHappened() {
        var webTarget = ClientBuilder.newClient().target(ApplicationTestUtils.BASE_URI + "error");
        var response = webTarget.request().get().readEntity(String.class);

        assertThat(response)
            .contains("We are the champions, my friend");
    }

}
