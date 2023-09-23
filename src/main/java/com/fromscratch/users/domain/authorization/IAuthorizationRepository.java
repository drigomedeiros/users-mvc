package com.fromscratch.users.domain.authorization;

import com.fromscratch.users.domain.users.User;

public interface IAuthorizationRepository {

    boolean findByTokenAndResource(String token, String resource);
    User listResources(String string);

}
