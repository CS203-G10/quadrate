package cs203t10.quadrate.algorithm;

import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.location.Location;
import cs203t10.quadrate.interval.IntervalService;
import cs203t10.quadrate.location.LocationService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scheduler {
    private IntervalService intervalService;
    private LocationService locationService;
    // 2,3,4,5,6,7,1
    // 0,1,2,3,,4,5,6,7
    private static final HashMap<Integer, Integer> dateConversion = new HashMap<>();
    static {
        dateConversion.put(2, 0);
        dateConversion.put(3, 1);
        dateConversion.put(4, 2);
        dateConversion.put(5, 3);
        dateConversion.put(6, 6);
        dateConversion.put(7, 5);
        dateConversion.put(1, 6);
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void schedule() {
        // convertDatetime(getStartTime());
        List<Interval> intervals = intervalService.getAllIntervalsBetween(getStartTime(), getEndTime());
        List<Interval> repeatedIntervals = intervalService.getRepeatingIntervals();
        // set the correct date for repeating intervals
        for (Interval i : repeatedIntervals) {
            // if the repeated interval startdate is later than next week
            // skip it
            if (i.getStartTime().getTime() > getEndTime().getTime()) {
                continue;
            }
            i.setStartTime(convertDatetime(i.getStartTime()));
            i.setEndTime(convertDatetime(i.getEndTime()));

            intervals.add(i);
        }

        List<Location> locations = locationService.getAllLocations();

        List<Interval> assignedIntervals = Algorithm.processIntervals(intervals, locations);
        // save into db
    }

    private Timestamp convertDatetime(Timestamp datetime) {
        Calendar originalDate = Calendar.getInstance();
        originalDate.setTimeInMillis(datetime.getTime());
        int dayOfWeek = originalDate.get(Calendar.DAY_OF_WEEK);

        // algo runs on sat or sun
        // find the date for upcoming monday
        Calendar updatedDate = Calendar.getInstance();
        while (updatedDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            updatedDate.add(Calendar.DATE, 1);
        }

        // add no of days to upcoming monday
        // depneds on day of week of the original date
        updatedDate.add(Calendar.DATE, dateConversion.get(dayOfWeek));

        String updatedDateStr = dateFormat.format(updatedDate.getTime());
        int[] dateArr = Stream.of(updatedDateStr.split("-")).mapToInt(Integer::parseInt).toArray();

        // change the date of original date
        originalDate.set(dateArr[0], dateArr[1] - 1, dateArr[2]);

        // System.out.println(originalDate);

        return new Timestamp(originalDate.getTimeInMillis());
    }

    // algo runs on sat or sun
    // get upcoming monday
    private Timestamp getStartTime() {
        Calendar cal = Calendar.getInstance();
        // algo runs on sat or sun
        // find the date for upcoming monday
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DATE, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    // get upcoming sunday
    private Timestamp getEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getStartTime().getTime());
        cal.add(Calendar.DATE, 6);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }
}
