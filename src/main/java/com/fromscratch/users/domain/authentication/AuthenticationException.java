package com.fromscratch.users.domain.authentication;

public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 7974586192L;
    
    public AuthenticationException(String message) {
        super(message);
    }

}
