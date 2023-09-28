package com.fromscratch.users.application.error;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.glassfish.jersey.server.mvc.Viewable;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/error")
public class ErrorPage {

    private final Map<String, String> errorMessages;

    public ErrorPage(){
        errorMessages = new HashMap<>();
        errorMessages.put("401", "You are not authorized to access this content");
        errorMessages.put("404", "You are trying to access a resource that does not exist.");
        errorMessages.put("424", "Ooops! Some of our external resources we rely on are not available. Wait and try again.");
        errorMessages.put("503", "Ooops! Server is not available. Trying to fix it.");
    }

    @GET
    public Response home(@QueryParam("code") String code) {
        var model = new HashMap<String, Object>();
        model.put("errorCode", Optional.ofNullable(code).orElse("500"));
        model.put("errorMessage", errorMessages.getOrDefault(code, "We are the champions, my friend!"));
        
        return Response.ok(new Viewable("/error", model)).build();
    }
    
}

