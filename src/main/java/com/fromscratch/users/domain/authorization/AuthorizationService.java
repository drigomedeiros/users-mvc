package com.fromscratch.users.domain.authorization;

import com.fromscratch.users.domain.users.User;

public class AuthorizationService {

    private final IAuthorizationRepository authorizationRepository;

    public AuthorizationService(IAuthorizationRepository authorizationRepository) {
        this.authorizationRepository = authorizationRepository;
    }

    public boolean authorize(AuthorizationRequest request) {
        if (!authorizationRepository.findByTokenAndResource(request.token(), request.resource())) {
            throw new AuthorizationException();
        }
        return true;
    }

    public User listResources(String token) {
        return authorizationRepository.listResources(token);
    }

}
