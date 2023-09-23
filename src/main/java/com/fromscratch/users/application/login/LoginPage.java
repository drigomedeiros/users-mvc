package com.fromscratch.users.application.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.server.mvc.Template;
import com.fromscratch.users.domain.authentication.AuthenticationException;
import com.fromscratch.users.domain.authentication.AuthenticationRequest;
import com.fromscratch.users.domain.authentication.AuthenticationService;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class LoginPage {

    private static final String ERROR_MESSAGE = "Incorrect login or password";
    private static final Logger logger = Logger.getLogger(LoginPage.class.getName());
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationService authenticationService;

    @Inject
    public LoginPage(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
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

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = "/login")
    public Map<String, Object> getAuthenticationForm(@QueryParam("unauthorized") boolean unauthorized) {
        Map<String, Object> model = new HashMap<>();
        model.put("formTitle", "Users");
        model.put("showError", false);
        if(unauthorized){
            model.put("showError", true);
            model.put("errorMessage", "You are not authorized to access this page");
        }
        return model;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response processAuthentication(@FormParam("username") String username, @FormParam("password") String password) {
        try {
            String token = authenticationService.authenticate(new AuthenticationRequest(username, password));
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            request.getRequestDispatcher("/app/home").forward(request, response);
            return Response.ok().build();
        } catch (ServletException | IOException e) {
            logger.log(Level.SEVERE, "The server was not able to redirect the request", e);
            return Response.status(Response.Status.UNAUTHORIZED).entity(ERROR_MESSAGE).build();
        } catch (AuthenticationException e) {
            logger.warning(e.getMessage() + " - " + username);
            return Response.status(Response.Status.UNAUTHORIZED).entity(ERROR_MESSAGE).build();
        }
    }

}
