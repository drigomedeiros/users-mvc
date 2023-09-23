package com.fromscratch.users.domain.users;

import java.util.List;

public class ListUsersService {

    private final IUserRepository userRepository;

    public ListUsersService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> list() {
        return userRepository.findAll();
    }

}
