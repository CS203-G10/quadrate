package cs203t10.quadrate.interval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;
import java.sql.Timestamp;

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
        // check if creator exists
        userService.getUser(interval.getCreator().getUsername());

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

    @Transactional
    public List<Interval> createAllIntervals(List<Interval> intervals)
            throws IntervalExistsException, UserNotFoundException, LocationNotFoundException {
        List<Interval> ret = new LinkedList<>();
        for (Interval interval : intervals) {
            ret.add(createInterval(interval));
        }
        return ret;
    }

    public List<Interval> getAllIntervals() {
        return intervalRepository.findAll();
    }

    public List<Interval> getAllIntervalsBetween(Timestamp startTime, Timestamp endTime) {
        return intervalRepository.findAllByStartTimeBetween(startTime, endTime);
    }

    public List<Interval> getRepeatingIntervals() {
        return intervalRepository.findByIsRepeated(true);

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
        // check if interval exists
        Interval existedInterval = getInterval(id);
        // check if location exists
        locationService.getLocation(interval.getLocation().getId());
        // check if creator exists
        userService.getUser(interval.getCreator().getUsername());

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

        return intervalRepository.save(existedInterval);
    }

    @Transactional
    public Interval removeInterval(Long id) throws IntervalNotFoundException {
        Interval interval = getInterval(id);
        intervalRepository.deleteById(id);
        return interval;
    }
}
