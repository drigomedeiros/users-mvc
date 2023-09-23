package com.fromscratch.users.domain.users;

import java.util.List;

import com.fromscratch.users.domain.authorization.Resource;

public record User (String userLogin, String userName, List<Resource> resources) {}
