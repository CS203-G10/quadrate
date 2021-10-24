package cs203t10.quadrate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.sql.Timestamp;

@ResponseStatus(HttpStatus.CONFLICT)
public class IntervalExistsException extends RuntimeException {
    public IntervalExistsException(String locationName, Timestamp startTime, Timestamp endTime) {
        super("Interval with location '" + locationName + "', start time '" + startTime + "', end time '" + endTime
                + "' has some confliction.");
    }
}
