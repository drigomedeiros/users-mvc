package com.fromscratch.users.domain.authentication;

import java.util.Optional;

public interface IAuthenticationRepository {

    Optional<String> findByLoginAndPassword(String userLogin, String userPassword);

}
