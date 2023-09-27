package com.fromscratch.users.application.login;

import java.util.Optional;
import java.util.logging.Logger;

import com.fromscratch.users.domain.authentication.AuthenticationException;
import com.fromscratch.users.domain.authentication.AuthenticationRequest;
import com.fromscratch.users.domain.authentication.AuthenticationService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/login")
public class LoginPage {

    private static final String ERROR_MESSAGE = "Incorrect login or password";
    private static final Logger logger = Logger.getLogger(LoginPage.class.getName());
    private AuthenticationService authenticationService;

    public LoginPage() {
        this.authenticationService = new AuthenticationService((user, password) -> {
            if(!user.equals("1") || !password.equals("1")){
                return Optional.empty();
            }
            return Optional.of("dummyToken");
        });
    }

    public LoginPage(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processAuthentication(AuthenticationRequest authenticationRequest) {
        try {
            String token = authenticationService.authenticate(authenticationRequest);
            return Response.ok().header(HttpHeaders.AUTHORIZATION, token).build();
        } catch (AuthenticationException e) {
            logger.warning(e.getMessage() + " - " + authenticationRequest.userLogin());
            return Response.status(Response.Status.UNAUTHORIZED).entity(ERROR_MESSAGE).build();
        }
    }

}
