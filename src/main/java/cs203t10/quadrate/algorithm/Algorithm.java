package cs203t10.quadrate.algorithm;

import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.interval.IntervalService;
import cs203t10.quadrate.location.Location;
import cs203t10.quadrate.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithm {

    public static List<Interval> processIntervals(Collection<Interval> intervals, Collection<Location> locations) {
        AlgorithmHelper helper = new AlgorithmHelper(locations);
        List<Interval> sortedIntervals = intervals.stream().sorted(Comparator.comparingDouble(Interval::getPriorityScore)).collect(Collectors.toList());
        helper.addAllIntervals(intervals);
        List<Interval> approvedIntervals = helper.getApprovedIntervals();
        approvedIntervals.forEach(interval -> interval.setType("assigned"));
        return approvedIntervals;
    }
}
