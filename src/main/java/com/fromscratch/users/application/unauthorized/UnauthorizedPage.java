package com.fromscratch.users.application.unauthorized;

import org.glassfish.jersey.server.mvc.Viewable;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/unauthorized")
public class UnauthorizedPage {

    @GET
    public Response home() {
        return Response.ok(new Viewable("/unauthorized")).build();
    }
    
}

