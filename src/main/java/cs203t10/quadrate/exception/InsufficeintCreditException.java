package cs203t10.quadrate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InsufficeintCreditException extends RuntimeException {
    public InsufficeintCreditException(Integer userCredit, Integer priority) {
        super("Insufficient credit - owned: '" + userCredit + "', required: '" + priority);
    }
}
