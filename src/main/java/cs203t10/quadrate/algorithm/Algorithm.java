package cs203t10.quadrate.algorithm;

import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.interval.IntervalRepository;
import cs203t10.quadrate.interval.IntervalService;
import cs203t10.quadrate.location.Location;
import cs203t10.quadrate.location.LocationService;
import cs203t10.quadrate.location.LocationRepository;
import lombok.RequiredArgsConstructor;

import org.hibernate.boot.model.source.spi.SingularAttributeSourceToOne;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Algorithm {

    public static List<Interval> processIntervals(Collection<Interval> intervals, Collection<Location> locations) {
        AlgorithmHelper helper = new AlgorithmHelper(locations);
        List<Interval> sortedIntervals = intervals.stream()
                .sorted((f1, f2) -> Long.compare(f2.getPriorityScore(), f1.getPriorityScore()))
                .collect(Collectors.toList());
        // System.out.println(sortedIntervals.size());
        // for (Interval i : sortedIntervals) {
        // System.out.println(i);
        // }
        helper.addAllIntervals(sortedIntervals);
        List<Interval> approvedIntervals = helper.getApprovedIntervals();
        approvedIntervals.forEach(interval -> interval.setType("assigned"));
        return approvedIntervals;
    }
}
