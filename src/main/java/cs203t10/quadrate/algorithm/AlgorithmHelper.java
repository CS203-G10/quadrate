package cs203t10.quadrate.algorithm;

import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.location.Location;
import cs203t10.quadrate.user.User;

import java.util.*;

public class AlgorithmHelper {
    private final Map<Long, IntervalTree<Interval>> locationIntervalsMap;
    private List<Interval> approvedIntervals;

    public AlgorithmHelper(Collection<Location> locations) {
        this.locationIntervalsMap = new HashMap<>();
        this.approvedIntervals = new ArrayList<>();
        locations.forEach(loc -> locationIntervalsMap.put(loc.getId(), new IntervalTree<>()));
    }

    public void addAllIntervals(Collection<Interval> intervals) {
        intervals.forEach(this::addInterval);
    }

    public void addInterval(Interval interval) {
        Location pointer = interval.getLocation();
        // check whether the capacity is exited for the current and all parent location
        while (pointer != null) {
            if (!withinCapacity(pointer, interval)) {
                return;
            }
            pointer = pointer.getParentLocation();
        }
        pointer = interval.getLocation();
        // add interval after ensure that capacity for all locations are not exit
        while (pointer != null) {
            locationIntervalsMap.get(pointer.getId()).insert(interval);
            pointer = pointer.getParentLocation();
        }
        Interval approvedInt = interval.clone();
        approvedInt.setIsRepeated(false);
        System.out.println("======================id======================");
        System.out.println(approvedInt.getId());
        approvedIntervals.add(approvedInt);
    }

    private boolean withinCapacity(Location location, Interval interval) {
        return countAttendees(location, interval) <= location.getCapacity();
    }

    private int countAttendees(Location location, Interval interval) {
        Set<User> attendees = new HashSet<>(interval.getAttendees());
        Iterator<Interval> overlappers = locationIntervalsMap.get(location.getId()).overlappers(interval);

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
