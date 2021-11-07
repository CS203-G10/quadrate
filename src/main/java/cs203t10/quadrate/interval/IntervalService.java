package cs203t10.quadrate.interval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

import cs203t10.quadrate.exception.IntervalNotFoundException;
import cs203t10.quadrate.exception.LocationNotFoundException;
import cs203t10.quadrate.exception.UserNotFoundException;
import cs203t10.quadrate.exception.IntervalExistsException;
import cs203t10.quadrate.user.*;
import cs203t10.quadrate.location.*;

@Service
@RequiredArgsConstructor
public class IntervalService {

    private final IntervalRepository intervalRepository;
    private final UserService userService;
    private final LocationService locationService;

    public Interval createInterval(Interval interval)
            throws IntervalExistsException, UserNotFoundException, LocationNotFoundException {
        // check if location exists
        locationService.getLocation(interval.getLocation().getId());

        // check for confliction for every attendees
        for (User attendee : interval.getAttendees()) {
            // chech whether user exists
            userService.getUser(attendee.getUsername());

            List<Interval> conflictedInterval = intervalRepository.findConflictedIntervals(interval.getId(),
                    interval.getLocation(), interval.getStartTime(), interval.getEndTime(), interval.getType(),
                    interval.getIsRepeated(), attendee.getId());

            if (!conflictedInterval.isEmpty()) {
                throw new IntervalExistsException(interval.getLocation().getName(), interval.getStartTime(),
                        interval.getEndTime());
            }
        }

        return intervalRepository.save(interval);
    }

    public List<Interval> getAllIntervals() {
        return intervalRepository.findAll();
    }

    public Interval getInterval(Long id) throws IntervalNotFoundException {
        return intervalRepository.findById(id).orElseThrow(() -> new IntervalNotFoundException(id));
    }

    public List<Interval> getIntervalsByType(String type) {
        return intervalRepository.findByType(type);
    }

    @Transactional
    public Interval updateInterval(Long id, Interval interval) throws IntervalNotFoundException,
            IntervalExistsException, UserNotFoundException, LocationNotFoundException {
        // check if location exists
        locationService.getLocation(interval.getLocation().getId());
        // check if interval exists
        Interval existedInterval = getInterval(id);

        // check any confliction for all attendees
        for (User attendee : interval.getAttendees()) {
            // chech whether user exists
            userService.getUser(attendee.getUsername());

            List<Interval> conflictedInterval = intervalRepository.findConflictedIntervals(id, interval.getLocation(),
                    interval.getStartTime(), interval.getEndTime(), interval.getType(), interval.getIsRepeated(),
                    attendee.getId());

            if (conflictedInterval.size() != 0) {
                throw new IntervalExistsException(interval.getLocation().getName(), interval.getStartTime(),
                        interval.getEndTime());
            }
        }

        existedInterval.setStartTime(interval.getStartTime());
        existedInterval.setEndTime(interval.getEndTime());
        existedInterval.setType(interval.getType());
        existedInterval.setIsRepeated(interval.getIsRepeated());
        existedInterval.setPriority(interval.getPriority());
        existedInterval.setCreator(interval.getCreator());
        existedInterval.getAttendees().clear();
        existedInterval.getAttendees().addAll(interval.getAttendees());
        existedInterval.setLocation(interval.getLocation());
        intervalRepository.save(existedInterval);

        return getInterval(id);
    }

    @Transactional
    public Interval removeInterval(Long id) throws IntervalNotFoundException {
        Interval interval = getInterval(id);
        intervalRepository.deleteById(id);
        return interval;
    }
}
