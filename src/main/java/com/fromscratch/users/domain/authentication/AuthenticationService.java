package com.fromscratch.users.domain.authentication;

public class AuthenticationService {

    private final IAuthenticationRepository authenticationRepository;

    public AuthenticationService(IAuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    public String authenticate(AuthenticationRequest request) {
        var userToken = authenticationRepository.findByLoginAndPassword(request.userLogin(), request.userPassword());
        return userToken.orElseThrow(() -> new AuthenticationException("Login or password are incorrect"));
    }

}
