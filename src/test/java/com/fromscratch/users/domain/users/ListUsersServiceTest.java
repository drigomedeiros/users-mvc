package com.fromscratch.users.domain.users;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import static java.util.Collections.emptyList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("List Users Service")
class ListUsersServiceTest {

    IUserRepository userRepository;
    ListUsersService listUsersService;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        listUsersService = new ListUsersService(userRepository);
    }

    @Test
    @DisplayName("should return all users")
    void shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User("userLogin1", "userName1", emptyList()), new User("userLogin2", "userName2", emptyList())));

        List<User> users = listUsersService.list();

        assertThat(users).containsExactly(
                new User("userLogin1", "userName1", emptyList()),
                new User("userLogin2", "userName2", emptyList())
        );
    }

    @Test
    @DisplayName("should return empty list when there is no user registered")
    void shouldReturnEmptyListWhenThereIsNoUserRegistered() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> users = listUsersService.list();

        assertThat(users).isEmpty();
    }
    
}
