package com.fromscratch.users.domain.users;

import java.util.List;

public interface IUserRepository {

    List<User> findAll();

}
