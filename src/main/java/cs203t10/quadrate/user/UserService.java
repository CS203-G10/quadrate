
package cs203t10.quadrate.user;

import cs203t10.quadrate.exception.UserExistsException;
import cs203t10.quadrate.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) throws UserExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserExistsException(user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional
    public User updateUser(String username, User user) throws UserExistsException, UserNotFoundException {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        }

        if (!user.getUsername().equals(username) && userRepository.existsByUsername(user.getUsername())) {
            throw new UserExistsException(user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.updateUser(username, user.getUsername(), user.getPassword(), user.getRole());
        return getUser(user.getUsername());
    }

    @Transactional
    public User removeUser(String username) throws UserNotFoundException {
        User user = getUser(username);
        userRepository.deleteByUsername(username);
        return user;
    }
}