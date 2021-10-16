package cs203t10.quadrate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User '" + username + "' not found.");
    }

    public UserNotFoundException(Long id) {
        super("User with ID '" + id + "' not found.");
    }
}
