package cs203t10.quadrate.algorithm;

import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.location.Location;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithm {

    public static List<Interval> processIntervals(Collection<Interval> intervals, Collection<Location> locations) {
        AlgorithmHelper helper = new AlgorithmHelper(locations);
        List<Interval> sortedIntervals = intervals.stream()
                .sorted((f1, f2) -> Long.compare(f2.getPriorityScore(), f1.getPriorityScore()))
                .collect(Collectors.toList());
        helper.addAllIntervals(sortedIntervals);
        List<Interval> approvedIntervals = helper.getApprovedIntervals();
        approvedIntervals.forEach(interval -> interval.setType("assigned"));
        return approvedIntervals;
    }
}
