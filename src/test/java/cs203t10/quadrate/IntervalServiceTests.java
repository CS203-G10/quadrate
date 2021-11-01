package cs203t10.quadrate;

import cs203t10.quadrate.exception.IntervalExistsException;
import cs203t10.quadrate.exception.IntervalNotFoundException;
import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.interval.IntervalService;
import cs203t10.quadrate.interval.IntervalRepository;
import cs203t10.quadrate.user.User;
import cs203t10.quadrate.location.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IntervalServiceTests {

    @Mock
    private IntervalRepository intervalRepository;

    @InjectMocks
    private IntervalService intervalService;

    // tests for create interval
    @Test
    void createInterval_NewInterval_ReturnNewInterval() {
        Interval newInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(),
                getLocation());

        when(intervalRepository.findConflictedIntervals(newInterval.getLocation().getId(), newInterval.getStartTime(),
                newInterval.getEndTime(), newInterval.getType(), newInterval.getId())).thenReturn(List.of());
        when(intervalRepository.save(any(Interval.class))).then(returnsFirstArg());

        Interval returnedInterval = intervalService.createInterval(newInterval);
        assertEquals(returnedInterval, newInterval);

        verify(intervalRepository).findConflictedIntervals(newInterval.getLocation().getId(),
                newInterval.getStartTime(), newInterval.getEndTime(), newInterval.getType(), newInterval.getId());
        verify(intervalRepository).save(newInterval);
        verify(intervalRepository, times(1)).save(newInterval);
        verifyNoMoreInteractions(intervalRepository);
    }

    @Test
    void createInterval_ConflictedInterval_ThrowIntervalExistsException() {
        Interval newInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(),
                getLocation());

        when(intervalRepository.findConflictedIntervals(newInterval.getLocation().getId(), newInterval.getStartTime(),
                newInterval.getEndTime(), newInterval.getType(), newInterval.getId())).thenReturn(List.of(newInterval));

        assertThrows(IntervalExistsException.class, () -> intervalService.createInterval(newInterval));

        verify(intervalRepository).findConflictedIntervals(newInterval.getLocation().getId(),
                newInterval.getStartTime(), newInterval.getEndTime(), newInterval.getType(), newInterval.getId());
        verifyNoMoreInteractions(intervalRepository);
    }

    // tests for get interval/intervals
    @Test
    void getAllIntervals_ReturnAllIntervals() {
        List<Interval> intervals = List
                .of(new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(), getLocation()));

        when(intervalRepository.findAll()).thenReturn(intervals);

        assertEquals(intervalService.getAllIntervals(), intervals);

        verify(intervalRepository).findAll();
        verifyNoMoreInteractions(intervalRepository);
    }

    @Test
    void getInterval_IntervalExists_ReturnInterval() {
        Interval interval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(), getLocation());

        when(intervalRepository.findById(interval.getId())).thenReturn(Optional.of(interval));

        Interval returnedInterval = intervalService.getInterval(interval.getId());
        assertEquals(interval, returnedInterval);

        verify(intervalRepository).findById(interval.getId());
        verifyNoMoreInteractions(intervalRepository);
    }

    @Test
    void getInterval_IntervalNotFound_ThrowIntervalNotFoundException() {
        Interval interval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(), getLocation());

        when(intervalRepository.findById(interval.getId())).thenReturn(Optional.empty());

        assertThrows(IntervalNotFoundException.class, () -> intervalService.getInterval(interval.getId()));

        verify(intervalRepository).findById(interval.getId());
        verifyNoMoreInteractions(intervalRepository);
    }

    // tests for update interval
    @Test
    void updateInterval_IntervalExistsAndNoException_ReturnUpdatedInterval() {
        Interval updatedInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(),
                getLocation());

        when(intervalRepository.existsById(updatedInterval.getId())).thenReturn(true);
        when(intervalRepository.findConflictedIntervals(updatedInterval.getLocation().getId(),
                updatedInterval.getStartTime(), updatedInterval.getEndTime(), updatedInterval.getType(),
                updatedInterval.getId())).thenReturn(List.of());
        when(intervalRepository.updateInterval(updatedInterval.getId(), updatedInterval.getStartTime(),
                updatedInterval.getEndTime(), updatedInterval.getType(), updatedInterval.isRepeated(),
                updatedInterval.getPriority(), updatedInterval.getUser().getId(),
                updatedInterval.getLocation().getId())).thenReturn(1);
        when(intervalRepository.findById(updatedInterval.getId())).thenReturn(Optional.of(updatedInterval));

        Interval returnedInterval = intervalService.updateInterval(updatedInterval.getId(), updatedInterval);
        assertEquals(returnedInterval, updatedInterval);

        verify(intervalRepository).existsById(updatedInterval.getId());
        verify(intervalRepository).findConflictedIntervals(updatedInterval.getLocation().getId(),
                updatedInterval.getStartTime(), updatedInterval.getEndTime(), updatedInterval.getType(),
                updatedInterval.getId());
        verify(intervalRepository).updateInterval(updatedInterval.getId(), updatedInterval.getStartTime(),
                updatedInterval.getEndTime(), updatedInterval.getType(), updatedInterval.isRepeated(),
                updatedInterval.getPriority(), updatedInterval.getUser().getId(),
                updatedInterval.getLocation().getId());
        verify(intervalRepository).findById(updatedInterval.getId());
        verifyNoMoreInteractions(intervalRepository);
    }

    @Test
    void updateInterval_IntervalNotFound_ThrowIntervalnotFoundException() {
        Interval updatedInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(),
                getLocation());

        when(intervalRepository.existsById(updatedInterval.getId())).thenReturn(false);

        assertThrows(IntervalNotFoundException.class,
                () -> intervalService.updateInterval(updatedInterval.getId(), updatedInterval));

        verify(intervalRepository).existsById(updatedInterval.getId());
        verifyNoMoreInteractions(intervalRepository);
    }

    @Test
    void updateInterval_ConflictingIntervals_ThrowIntervalExistsException() {
        Interval updatedInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(),
                getLocation());

        when(intervalRepository.existsById(updatedInterval.getId())).thenReturn(true);
        when(intervalRepository.findConflictedIntervals(updatedInterval.getLocation().getId(),
                updatedInterval.getStartTime(), updatedInterval.getEndTime(), updatedInterval.getType(),
                updatedInterval.getId())).thenReturn(List.of(updatedInterval));

        assertThrows(IntervalExistsException.class,
                () -> intervalService.updateInterval(updatedInterval.getId(), updatedInterval));

        verify(intervalRepository).existsById(updatedInterval.getId());
        verify(intervalRepository).findConflictedIntervals(updatedInterval.getLocation().getId(),
                updatedInterval.getStartTime(), updatedInterval.getEndTime(), updatedInterval.getType(),
                updatedInterval.getId());
        verifyNoMoreInteractions(intervalRepository);
    }

    // tests for delete interval
    @Test
    void removeInterval_IntervalFound_ReturnInterval() {
        Interval interval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(), getLocation());

        when(intervalRepository.findById(interval.getId())).thenReturn(Optional.of(interval));

        Interval deletedInterval = intervalService.removeInterval(interval.getId());
        assertEquals(interval, deletedInterval);

        verify(intervalRepository).findById(interval.getId());
        verify(intervalRepository, times(1)).deleteById(interval.getId());
        verifyNoMoreInteractions(intervalRepository);
    }

    @Test
    void removeInterval_IntervalNotFound_ThrowIntervalNotFoundException() {
        Interval interval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getUser(), getLocation());

        when(intervalRepository.findById(interval.getId())).thenReturn(Optional.empty());

        assertThrows(IntervalNotFoundException.class, () -> intervalService.removeInterval(interval.getId()));

        verify(intervalRepository).findById(interval.getId());
        verifyNoMoreInteractions(intervalRepository);
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

    private User getUser() {
        return new User("YuXuan");
    }

    private Location getLocation() {
        return new Location("Desk 1", 1, true);
    }
}
