package com.fromscratch.users.domain.authorization;

public record AuthorizationRequest(String token, String resource) {}
