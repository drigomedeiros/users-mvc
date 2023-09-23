package com.fromscratch.users.domain.authorization;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fromscratch.users.domain.users.User;

@DisplayName("Authorization Service")
class AuthorizationServiceTest {

    IAuthorizationRepository authorizationRepository;
    AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        authorizationRepository = mock(IAuthorizationRepository.class);
        when(authorizationRepository.findByTokenAndResource("dummyToken", "list-users")).thenReturn(true);
        when(authorizationRepository.findByTokenAndResource(eq("dummyToken"), not(eq("list-users")))).thenReturn(false);
        when(authorizationRepository.findByTokenAndResource(not(eq("dummyToken")), eq("list-users"))).thenReturn(false);
        when(authorizationRepository.findByTokenAndResource(not(eq("dummyToken")), not(eq("list-users")))).thenReturn(false);
        
        authorizationService = new AuthorizationService(authorizationRepository);
    }

    @Test
    @DisplayName("should authorize a user to a resource")
    void shouldAuthorizeAUser() {
        AuthorizationRequest request = new AuthorizationRequest("dummyToken", "list-users");

        boolean authorized = authorizationService.authorize(request);

        assertThat(authorized).isTrue();
    }

    @Test
    @DisplayName("should list user resources")
    void shouldListUserResources() {
        var userInRepository = new User("admin@fromscratch.com", "John", List.of(new Resource("list-users", "Users", "/app/users", "GET")));
        when(authorizationRepository.listResources("dummyToken")).thenReturn(userInRepository);
        var user = authorizationService.listResources("dummyToken");

        assertThat(user.userLogin()).isEqualTo("admin@fromscratch.com");
        assertThat(user.userName()).isEqualTo("John");
        assertThat(user.resources())
            .isNotEmpty()
            .containsExactly(new Resource("list-users", "Users", "/app/users", "GET"));
    }

    @Test
    @DisplayName("should not authorize a user when login or password are incorrect")
    void shouldNotAuthenticateAUserWhenLoginOrPasswordAreIncorrect() {
        AuthorizationRequest wrongTokenRequest = new AuthorizationRequest( "dummyToken2", "list-users");
        AuthorizationRequest wrongRoleRequest = new AuthorizationRequest( "dummyToken", "list-roles");
        AuthorizationRequest wrongTokenAndRoleRequest = new AuthorizationRequest( "dummyToken2", "list-roles");

        assertThatThrownBy(() -> authorizationService.authorize(wrongTokenRequest))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("Either token is not valid or the user does not have permission to perform this action");
        assertThatThrownBy(() -> authorizationService.authorize(wrongRoleRequest))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("Either token is not valid or the user does not have permission to perform this action");
        assertThatThrownBy(() -> authorizationService.authorize(wrongTokenAndRoleRequest))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("Either token is not valid or the user does not have permission to perform this action");
    }
    
}
