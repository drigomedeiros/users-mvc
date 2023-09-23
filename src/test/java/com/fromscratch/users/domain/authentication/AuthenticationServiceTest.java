package com.fromscratch.users.domain.authentication;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Authentication Service")
class AuthenticationServiceTest {

    IAuthenticationRepository userRepository;
    AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(IAuthenticationRepository.class);
        when(userRepository.findByLoginAndPassword("mymail@test.com", "123456")).thenReturn(Optional.of("dummyToken"));
        when(userRepository.findByLoginAndPassword(eq("mymail@test.com"), not(eq("123456")))).thenReturn(Optional.empty());
        when(userRepository.findByLoginAndPassword(not(eq("mymail@test.com")), eq("123456"))).thenReturn(Optional.empty());
        when(userRepository.findByLoginAndPassword(not(eq("mymail@test.com")), not(eq("123456")))).thenReturn(Optional.empty());
        
        authenticationService = new AuthenticationService(userRepository);
    }

    @Test
    @DisplayName("should authenticate a user")
    void shouldAuthenticateAUser() {
        AuthenticationRequest request = new AuthenticationRequest("mymail@test.com", "123456");

        String authenticated = authenticationService.authenticate(request);

        assertThat(authenticated).isNotNull().isNotBlank();
    }

    @Test
    @DisplayName("should not authenticate a user when login or password are incorrect")
    void shouldNotAuthenticateAUserWhenLoginOrPasswordAreIncorrect() {
        AuthenticationRequest wrongPasswordRequest = new AuthenticationRequest( "mymail@test.com", "654321");
        AuthenticationRequest wrongLoginRequest = new AuthenticationRequest( "mymail2@test.com", "123456");
        AuthenticationRequest wrongLoginAndPasswordRequest = new AuthenticationRequest( "mymail2@test.com", "1234567");

        assertThatThrownBy(() -> authenticationService.authenticate(wrongPasswordRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Login or password are incorrect");
        assertThatThrownBy(() -> authenticationService.authenticate(wrongLoginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Login or password are incorrect");
        assertThatThrownBy(() -> authenticationService.authenticate(wrongLoginAndPasswordRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Login or password are incorrect");
    }
    
}
