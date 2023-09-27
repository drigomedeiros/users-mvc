package com.fromscratch.users.application.users;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.glassfish.jersey.server.mvc.Viewable;

import com.fromscratch.users.domain.authorization.Resource;
import com.fromscratch.users.domain.users.IUserRepository;
import com.fromscratch.users.domain.users.User;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class ListUsersPage {

    private HttpServletRequest request;
    private IUserRepository userRepository;
    
    @Inject
    public ListUsersPage(HttpServletRequest request) {
        this.request = request;
        this.userRepository = () -> List.of(
            new User("1", "John", List.of(new Resource("home", "Home", "/app/", "GET"),
                                            new Resource("users", "Users", "/app/users", "GET")))
        );        
    }

    @GET
    public Response users() {
        return getRequestAuthorizationHeader()
                    .map(this::getUsersPageContent)
                    .orElse(Response.seeOther(URI.create("/app/error?code=401")).build());
    }

    private Response getUsersPageContent(String token) {
        var model = new HashMap<String, Object>();
        var users = userRepository.findAll();
        model.put("users", users);
        return Response.ok(new Viewable("/users", model)).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
    }

    private Optional<String> getRequestAuthorizationHeader() {
        return request.getHeader(HttpHeaders.AUTHORIZATION) == null ? 
                Optional.empty() : 
                Optional.of(request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", ""));
    }

}
