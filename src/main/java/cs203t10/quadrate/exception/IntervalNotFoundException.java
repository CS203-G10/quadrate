package cs203t10.quadrate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IntervalNotFoundException extends RuntimeException{
    public IntervalNotFoundException(Long id) {
        super("Interval with ID '" + id + "' not found.");
    }
}
