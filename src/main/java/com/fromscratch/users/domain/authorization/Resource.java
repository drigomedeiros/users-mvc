package com.fromscratch.users.domain.authorization;

public record Resource(String name, String description, String path, String method) {
    
}
