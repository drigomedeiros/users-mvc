package com.fromscratch.users.infrastructure.repositories;

import java.util.List;

import com.fromscratch.users.domain.authorization.IAuthorizationRepository;
import com.fromscratch.users.domain.authorization.Resource;
import com.fromscratch.users.domain.users.User;

public class AuthorizationRepository implements IAuthorizationRepository {

    @Override
    public boolean findByTokenAndResource(String token, String resource) {
        return token.equals("dummyToken") 
                    && (resource.equals("/app/users")
                    || resource.equals("/app/"));
    }

    @Override
    public User listResources(String string) {

        return new User("admin@fromscratch.com", 
                        "John", 
                        List.of(
                            new Resource("home", "Home", "/app/home/initial", "GET"),
                            new Resource("list-users", "Users", "/app/users", "GET")));
    }

}
