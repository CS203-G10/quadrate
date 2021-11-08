package cs203t10.quadrate;

import cs203t10.quadrate.exception.IntervalExistsException;
import cs203t10.quadrate.exception.IntervalNotFoundException;
import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.interval.IntervalService;
import cs203t10.quadrate.interval.IntervalRepository;
import cs203t10.quadrate.user.*;
import cs203t10.quadrate.location.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IntervalServiceTests {

        @Mock
        private IntervalRepository intervalRepository;

        @Mock
        private LocationService locationService;

        @Mock
        private UserService userService;

        @InjectMocks
        private IntervalService intervalService;

        // tests for create interval
        @Test
        void createInterval_NewInterval_ReturnNewInterval() {
                Interval newInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getCreator(),
                                getAttendees(), getBookedLocation());

                when(locationService.getLocation(getBookedLocation().getId())).thenReturn(getBookedLocation());
                when(userService.getUser(getCreator().getUsername())).thenReturn(getCreator());
                when(intervalRepository.findConflictedIntervals(newInterval.getId(), newInterval.getLocation(),
                                newInterval.getStartTime(), newInterval.getEndTime(), newInterval.getType(),
                                newInterval.getIsRepeated(), newInterval.getId())).thenReturn(List.of());
                when(intervalRepository.save(any(Interval.class))).then(returnsFirstArg());

                Interval returnedInterval = intervalService.createInterval(newInterval);
                assertEquals(returnedInterval, newInterval);

                verify(locationService).getLocation(getBookedLocation().getId());
                // check all attendees and the creator exists
                verify(userService, times(getAttendees().size() + 1)).getUser(any(String.class));
                // check no confliction for all attendees
                verify(intervalRepository, times(getAttendees().size())).findConflictedIntervals(newInterval.getId(),
                                newInterval.getLocation(), newInterval.getStartTime(), newInterval.getEndTime(),
                                newInterval.getType(), newInterval.getIsRepeated(), getCreator().getId());
                verify(intervalRepository).save(newInterval);
                verify(intervalRepository, times(1)).save(newInterval);
                verifyNoMoreInteractions(intervalRepository);
        }

        @Test
        void createInterval_ConflictedInterval_ThrowIntervalExistsException() {
                Interval newInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getCreator(),
                                getAttendees(), getBookedLocation());

                when(locationService.getLocation(getBookedLocation().getId())).thenReturn(getBookedLocation());
                when(userService.getUser(getCreator().getUsername())).thenReturn(getCreator());
                when(intervalRepository.findConflictedIntervals(newInterval.getId(), newInterval.getLocation(),
                                newInterval.getStartTime(), newInterval.getEndTime(), newInterval.getType(),
                                newInterval.getIsRepeated(), newInterval.getId())).thenReturn(List.of(newInterval));

                assertThrows(IntervalExistsException.class, () -> intervalService.createInterval(newInterval));

                verify(locationService).getLocation(getBookedLocation().getId());
                // one time to check esists as creator, one time to check exists as attendees
                verify(userService, times(2)).getUser(any(String.class));
                // check no confliction for all attendees
                verify(intervalRepository).findConflictedIntervals(newInterval.getId(), newInterval.getLocation(),
                                newInterval.getStartTime(), newInterval.getEndTime(), newInterval.getType(),
                                newInterval.getIsRepeated(), getCreator().getId());
                verifyNoMoreInteractions(intervalRepository);
        }

        // tests for get interval/intervals
        @Test
        void getAllIntervals_ReturnAllIntervals() {
                List<Interval> intervals = List.of(new Interval(getStartTime(), getEndTime(), "Preference", true, 0,
                                getCreator(), getAttendees(), getBookedLocation()));

                when(intervalRepository.findAll()).thenReturn(intervals);

                assertEquals(intervalService.getAllIntervals(), intervals);

                verify(intervalRepository).findAll();
                verifyNoMoreInteractions(intervalRepository);
        }

        @Test
        void getInterval_IntervalExists_ReturnInterval() {
                Interval interval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getCreator(),
                                getAttendees(), getBookedLocation());

                when(intervalRepository.findById(interval.getId())).thenReturn(Optional.of(interval));

                Interval returnedInterval = intervalService.getInterval(interval.getId());
                assertEquals(interval, returnedInterval);

                verify(intervalRepository).findById(interval.getId());
                verifyNoMoreInteractions(intervalRepository);
        }

        @Test
        void getInterval_IntervalNotFound_ThrowIntervalNotFoundException() {
                when(intervalRepository.findById(any(Long.class))).thenReturn(Optional.empty());

                assertThrows(IntervalNotFoundException.class, () -> intervalService.getInterval(any(Long.class)));

                verify(intervalRepository).findById(any(Long.class));
                verifyNoMoreInteractions(intervalRepository);
        }

        // tests for update interval
        @Test
        void updateInterval_IntervalExistsAndNoException_ReturnUpdatedInterval() {
                Interval updatedInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0,
                                getCreator(), getAttendees(), getBookedLocation());
                updatedInterval.setId(1L);

                when(intervalRepository.findById(updatedInterval.getId())).thenReturn(Optional.of(updatedInterval));
                when(locationService.getLocation(getBookedLocation().getId())).thenReturn(getBookedLocation());
                when(userService.getUser(getCreator().getUsername())).thenReturn(getCreator());
                when(intervalRepository.findConflictedIntervals(updatedInterval.getId(), updatedInterval.getLocation(),
                                updatedInterval.getStartTime(), updatedInterval.getEndTime(), updatedInterval.getType(),
                                updatedInterval.getIsRepeated(), updatedInterval.getCreator().getId()))
                                                .thenReturn(new ArrayList<Interval>());
                when(intervalRepository.save(updatedInterval)).then(returnsFirstArg());

                Interval returnedInterval = intervalService.updateInterval(updatedInterval.getId(), updatedInterval);
                assertEquals(returnedInterval, updatedInterval);

                verify(intervalRepository).findById(updatedInterval.getId());
                verify(locationService).getLocation(getBookedLocation().getId());
                // check all attendees and the creator exists
                verify(userService, times(getAttendees().size() + 1)).getUser(any(String.class));
                // check no confliction for all attendees
                verify(intervalRepository, times(getAttendees().size())).findConflictedIntervals(
                                updatedInterval.getId(), updatedInterval.getLocation(), updatedInterval.getStartTime(),
                                updatedInterval.getEndTime(), updatedInterval.getType(),
                                updatedInterval.getIsRepeated(), updatedInterval.getCreator().getId());
                verify(intervalRepository, times(1)).save(updatedInterval);
                verifyNoMoreInteractions(intervalRepository);
        }

        @Test
        void updateInterval_IntervalNotFound_ThrowIntervalnotFoundException() {
                Interval updatedInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0,
                                getCreator(), getAttendees(), getBookedLocation());
                updatedInterval.setId(1L);

                when(intervalRepository.findById(updatedInterval.getId())).thenReturn(Optional.empty());

                assertThrows(IntervalNotFoundException.class,
                                () -> intervalService.updateInterval(updatedInterval.getId(), updatedInterval));

                verify(intervalRepository).findById(updatedInterval.getId());
                verifyNoMoreInteractions(intervalRepository);
        }

        @Test
        void updateInterval_ConflictingIntervals_ThrowIntervalExistsException() {
                Interval updatedInterval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0,
                                getCreator(), getAttendees(), getBookedLocation());
                updatedInterval.setId(1L);

                when(intervalRepository.findById(updatedInterval.getId())).thenReturn(Optional.of(updatedInterval));
                when(locationService.getLocation(getBookedLocation().getId())).thenReturn(getBookedLocation());
                when(userService.getUser(getCreator().getUsername())).thenReturn(getCreator());
                when(intervalRepository.findConflictedIntervals(updatedInterval.getId(), updatedInterval.getLocation(),
                                updatedInterval.getStartTime(), updatedInterval.getEndTime(), updatedInterval.getType(),
                                updatedInterval.getIsRepeated(), updatedInterval.getCreator().getId()))
                                                .thenReturn(List.of(updatedInterval));

                assertThrows(IntervalExistsException.class,
                                () -> intervalService.updateInterval(updatedInterval.getId(), updatedInterval));

                verify(intervalRepository).findById(updatedInterval.getId());
                verify(locationService).getLocation(getBookedLocation().getId());
                // one time to check esists as creator, one time to check exists as attendees
                verify(userService, times(2)).getUser(any(String.class));
                // check no confliction for all attendees
                verify(intervalRepository).findConflictedIntervals(updatedInterval.getId(),
                                updatedInterval.getLocation(), updatedInterval.getStartTime(),
                                updatedInterval.getEndTime(), updatedInterval.getType(),
                                updatedInterval.getIsRepeated(), updatedInterval.getCreator().getId());
                verifyNoMoreInteractions(intervalRepository);
        }

        // tests for delete interval
        @Test
        void removeInterval_IntervalFound_ReturnInterval() {
                Interval interval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getCreator(),
                                getAttendees(), getBookedLocation());

                when(intervalRepository.findById(interval.getId())).thenReturn(Optional.of(interval));

                Interval deletedInterval = intervalService.removeInterval(interval.getId());
                assertEquals(interval, deletedInterval);

                verify(intervalRepository).findById(interval.getId());
                verify(intervalRepository, times(1)).deleteById(interval.getId());
                verifyNoMoreInteractions(intervalRepository);
        }

        @Test
        void removeInterval_IntervalNotFound_ThrowIntervalNotFoundException() {
                Interval interval = new Interval(getStartTime(), getEndTime(), "Preference", true, 0, getCreator(),
                                getAttendees(), getBookedLocation());

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
