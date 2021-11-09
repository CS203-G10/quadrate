package cs203t10.quadrate.algorithm;

import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.location.Location;
import cs203t10.quadrate.user.User;

import java.util.*;

public class AlgorithmHelper {
    private final Map<Location, IntervalTree<Interval>> locationIntervalsMap;
    private List<Interval> approvedIntervals;

    public AlgorithmHelper(Collection<Location> locations) {
        this.locationIntervalsMap = new HashMap<>();
        locations.forEach(loc -> locationIntervalsMap.put(loc, new IntervalTree<>()));
    }

    public void addAllIntervals(Collection<Interval> intervals) {
        intervals.forEach(this::addInterval);
    }

    public void addInterval(Interval interval) {
        Location pointer = interval.getLocation();
        while (pointer.hasParent()) {
            if (!withinCapacity(pointer, interval)) {
                return;
            }
            pointer = pointer.getParentLocation();
        }

        pointer = interval.getLocation();
        while (pointer.hasParent()) {
            locationIntervalsMap.get(pointer).insert(interval);
        }
        approvedIntervals.add(interval.clone());
    }

    private boolean withinCapacity(Location location, Interval interval) {
        return countAttendees(location, interval) > location.getCapacity();
    }

    private int countAttendees(Location location, Interval interval) {
        Set<User> attendees = new HashSet<>(interval.getAttendees());
        Iterator<Interval> overlappers = locationIntervalsMap.get(location).overlappers(interval);
        while (overlappers.hasNext()) {
            Interval overlapper = overlappers.next();
            attendees.addAll(overlapper.getAttendees());
        }
        return attendees.size();
    }

    public List<Interval> getApprovedIntervals() {
        return approvedIntervals;
    }
}
