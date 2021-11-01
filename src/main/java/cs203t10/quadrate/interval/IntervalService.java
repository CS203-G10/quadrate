package cs203t10.quadrate.interval;

import cs203t10.quadrate.exception.IntervalNotFoundException;
import cs203t10.quadrate.exception.IntervalExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
// import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IntervalService {

    private final IntervalRepository intervalRepository;

    public Interval createInterval(Interval interval) throws IntervalExistsException {
        List<Interval> conflictedInterval = intervalRepository.findConflictedIntervals(interval.getLocation().getId(),
                interval.getStartTime(), interval.getEndTime(), interval.getType(), interval.getId());

        System.out.println(conflictedInterval.size());
        System.out.println(interval);

        if (!conflictedInterval.isEmpty()) {
            throw new IntervalExistsException(interval.getLocation().getName(), interval.getStartTime(),
                    interval.getEndTime());
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
    public Interval updateInterval(Long id, Interval interval) {
        if (!intervalRepository.existsById(id)) {
            throw new IntervalNotFoundException(id);
        }

        List<Interval> conflictedInterval = intervalRepository.findConflictedIntervals(interval.getLocation().getId(),
                interval.getStartTime(), interval.getEndTime(), interval.getType(), id);

        System.out.println(conflictedInterval.size());
        System.out.println(interval);

        if (conflictedInterval.size() != 0) {
            throw new IntervalExistsException(interval.getLocation().getName(), interval.getStartTime(),
                    interval.getEndTime());
        }

        // Timestamp startTime, Timestamp endTime, String type, boolean isRepeated,
        // Integer priority, User user, List<User> attendees, Location location
        intervalRepository.updateInterval(id, interval.getStartTime(), interval.getEndTime(), interval.getType(),
                interval.isRepeated(), interval.getPriority(), interval.getUser().getId(),
                interval.getLocation().getId());

        return getInterval(id);
    }

    @Transactional
    public Interval removeInterval(Long id) throws IntervalNotFoundException {
        Interval interval = getInterval(id);
        intervalRepository.deleteById(id);
        return interval;
    }
}
