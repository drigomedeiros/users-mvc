package com.fromscratch.users.application.home;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.glassfish.jersey.server.mvc.Viewable;

import com.fromscratch.users.domain.authorization.AuthorizationService;
import com.fromscratch.users.domain.authorization.Resource;
import com.fromscratch.users.infrastructure.repositories.AuthorizationRepository;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Path("/home")
public class HomePage {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthorizationService authorizationService;
    
    @Inject
    public HomePage(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        var authorizationRepository = new AuthorizationRepository();
        this.authorizationService = new AuthorizationService(authorizationRepository);
    }

    @GET
    @Path("/initial")
    public Response homeInitialContent() {
        return getRequestAuthorizationHeader()
                    .map(this::getHomeInitialContent)
                    .orElse(Response.seeOther(URI.create("/app/?unauthorized=true")).build());
    }

    @GET
    public Response home() {
        return getRequestAuthorizationHeader()
                    .map(this::getHomePageSpaResponse)
                    .orElse(Response.status(Response.Status.UNAUTHORIZED).entity("You're not authorized to access this content").build());
    }

    @POST
    public Response homeAfterLogin() {
        return getResponseAuthorizationHeader()
                    .map(this::getHomePageSpaResponse)
                    .orElse(Response.seeOther(URI.create("/app/?unauthorized=true")).build());
    }

    private Response getHomeInitialContent(String token) {
        return Response.ok(new Viewable("/initialContent")).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
    }

    private Response getHomePageSpaResponse(String token) {
        var user = authorizationService.listResources(token);

        var model = new HashMap<String, Object>();
        model.put("userName", user.userName());
        model.put("resources", toMenuItem(user.resources()));

        return Response.ok(new Viewable("/home", model)).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
    }

    private List<MenuItem> toMenuItem(List<Resource> resources) {
        return resources.stream()
                .map(resource -> new MenuItem(resource.path(), resource.description(), resource.path().contains("/app/home")))
                .toList();
    }

    private Optional<String> getRequestAuthorizationHeader() {
        return request.getHeader(HttpHeaders.AUTHORIZATION) == null ? 
                Optional.empty() : 
                Optional.of(request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", ""));
    }

    private Optional<String> getResponseAuthorizationHeader() {
        return response.getHeader(HttpHeaders.AUTHORIZATION) == null ? 
                Optional.empty() : 
                Optional.of(response.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", ""));
    }

}
