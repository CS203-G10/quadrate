package cs203t10.quadrate;

import cs203t10.quadrate.algorithm.Algorithm;
import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.location.Location;
import cs203t10.quadrate.user.User;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AlgorithmTest {

    @Test
    void runAl_IntervalFound_ReturnIntervalsAdded() {
        // Timestamp.valueOf(LocalDateTime.of())
        Interval interval = new Interval(getStartTime(), getEndTime(), "Preference", true, 1, getCreator(),
                getAttendees(), getBookedLocation());

        List<Interval> approvedIntervals = Algorithm.processIntervals(List.of(interval),List.of(getBookedLocation()));

        assertEquals(approvedIntervals, List.of(interval));

        verify(intervalRepository).findById(interval.getId());
        verify(intervalRepository, times(1)).deleteById(interval.getId());
        verifyNoMoreInteractions(intervalRepository);
    }

    public List<Interval> getInterval(){
        Interval int1 = new Interval(getStartTime(), getEndTime(), "Preference", true, 1, new User("Yuxuan"),
                getAttendees(), getBookedLocation());
        Interval int2 = new Interval(getStartTime(), getEndTime(), "Preference", true, 2, getCreator(),
                getAttendees(), getBookedLocation());
        return List.of(new Interval());
    }

    private Timestamp getStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(2021, 10, 21);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    private Timestamp getEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(2021, 10, 21);
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    private User getCreator() {
        return new User("YuXuan");
    }

    private Location getBookedLocation() {
        return new Location("Desk 1", 1, true);
    }

    private Set<User> getAttendees() {
        Set<User> urs = new HashSet<>();
        urs.add(new User("YuXuan"));
        urs.add(new User("Yuki"));
        return urs;
    }
}
