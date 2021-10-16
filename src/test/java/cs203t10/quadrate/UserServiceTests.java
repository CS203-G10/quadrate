package cs203t10.quadrate;

import cs203t10.quadrate.exception.UserExistsException;
import cs203t10.quadrate.exception.UserNotFoundException;
import cs203t10.quadrate.user.User;
import cs203t10.quadrate.user.UserService;
import cs203t10.quadrate.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_NewUsername_ReturnNewUser() {
        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(userRepository.save(any(User.class))).then(returnsFirstArg());

        User newUser = new User("fangzhou");
        User returnedUser = userService.createUser(newUser);
        assertEquals(returnedUser, newUser);

        verify(userRepository).existsByUsername(newUser.getUsername());
        verify(userRepository, times(1)).save(newUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_ConflictingUsername_ThrowUserExistsException() {
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);

        User user = new User("fangzhou");
        assertThrows(UserExistsException.class,
                () -> userService.createUser(user));

        verify(userRepository).existsByUsername(user.getUsername());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getAllUsers_returnAllUsers() {
        List<User> users = List.of(
                new User("fangzhou"),
                new User("sin3142")
        );
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(userService.getAllUsers(), users);
        verify(userRepository).findAll();
    }

    @Test
    void getUser_UserExists_ReturnUser() {
        User user = new User("fangzhou");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User returnedUser = userService.getUser(user.getUsername());
        assertEquals(returnedUser, user);

        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void getUser_UserNotExist_ThrowUserNotFoundException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser("fangzhou"));

        verify(userRepository).findByUsername(any(String.class));
    }

    @Test
    void updateUser_UserExistsAndNoConflictingUsername_ReturnUpdatedUser() {
        User oldUser = new User("fangzhou");
        User updatedUser = new User("sin3142");
        when(userRepository.existsByUsername(oldUser.getUsername())).thenReturn(true);
        when(userRepository.existsByUsername(updatedUser.getUsername())).thenReturn(false);
        when(userRepository.updateUser(eq(oldUser.getUsername()), eq(updatedUser.getUsername()), any(), any())).thenReturn(1);
        when(userRepository.findByUsername(updatedUser.getUsername())).thenReturn(Optional.of(updatedUser));

        User returnedUser = userService.updateUser(oldUser.getUsername(), updatedUser);
        assertEquals(returnedUser, updatedUser);

        verify(userRepository).existsByUsername(oldUser.getUsername());
        verify(userRepository).existsByUsername(updatedUser.getUsername());
        verify(userRepository).updateUser(eq(oldUser.getUsername()), eq(updatedUser.getUsername()), any(), any());
        verify(userRepository).findByUsername(updatedUser.getUsername());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_UserNotExist_ThrowUserNotFoundException() {
        User oldUser = new User("fangzhou");
        when(userRepository.existsByUsername(oldUser.getUsername())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(oldUser.getUsername(), any(User.class)));

        verify(userRepository).existsByUsername(oldUser.getUsername());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_ConflictingUsername_ThrowUserExistsException() {
        User oldUser = new User("fangzhou");
        User updatedUser = new User("sin3142");
        when(userRepository.existsByUsername(oldUser.getUsername())).thenReturn(true);
        when(userRepository.existsByUsername(updatedUser.getUsername())).thenReturn(true);

        assertThrows(UserExistsException.class, () -> userService.updateUser(oldUser.getUsername(), updatedUser));

        verify(userRepository).existsByUsername(oldUser.getUsername());
        verify(userRepository).existsByUsername(updatedUser.getUsername());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void removeUser_UserExists_ReturnUser() {
        User user = new User("fangzhou");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.deleteByUsername(user.getUsername())).thenReturn(1);

        User returnedUser = userService.removeUser(user.getUsername());
        assertEquals(returnedUser, user);

        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository, times(1)).deleteByUsername(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void removeUser_UserNotExist_ThrowUserNotFoundException() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.removeUser("fangzhou"));

        verify(userRepository).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }
}
