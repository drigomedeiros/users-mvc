package com.fromscratch.users.domain.authorization;

public class AuthorizationException extends RuntimeException {

    private static final long serialVersionUID = -3802872787L;

    public AuthorizationException() {
        super("Either token is not valid or the user does not have permission to perform this action");
    }

}
