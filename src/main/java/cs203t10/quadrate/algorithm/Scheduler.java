package cs203t10.quadrate.algorithm;

import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.location.Location;
import cs203t10.quadrate.interval.IntervalService;
import cs203t10.quadrate.location.LocationService;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {
    private final IntervalService intervalService;
    private final LocationService locationService;
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

    // runs at 00:00 on saturday
    // cron format: <second> <minute> <hour> <day-of-month> <month> <day-of-week>
    // @Scheduled(cron = "0 0 * * 6")
    @Scheduled(cron = "45 06 05 * * *")
    public void schedule() {
        // get all intervals that fall between mon 0.0.0 to sun 23.59.59
        // excluding repeating intervals where date is not relavant
        List<Interval> intervals = intervalService.getAllIntervalsBetween(getStartTime(), getEndTime());

        // get all repeating intervals
        List<Interval> repeatedIntervals = intervalService.getRepeatingIntervals();
        // set the correct date for repeating intervals according to day of week
        for (Interval i : repeatedIntervals) {
            // if the repeated interval startdate is later than next week
            // skip it
            if (i.getStartTime().getTime() > getEndTime().getTime()) {
                continue;
            }

            // convert to correct date
            i.setStartTime(convertDatetime(i.getStartTime()));
            i.setEndTime(convertDatetime(i.getEndTime()));

            intervals.add(i);
        }

        // get all locations from db
        List<Location> locations = locationService.getAllLocations();

        // run the algo to get assigned intervals
        List<Interval> assignedIntervals = Algorithm.processIntervals(intervals, locations);

        // save the assigned intervals into db
        intervalService.createAllIntervals(assignedIntervals);

        // testing
        List<Interval> fetched = intervalService.getAllAssignedIntervalsBetween(getStartTime(), getEndTime(),
                "assigned");
        for (Interval i : fetched) {
            System.out.println(i);
        }
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

        return new Timestamp(originalDate.getTimeInMillis());
    }

    // algo runs on sat or sun
    // get upcoming monday as start time
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

    // get upcoming sunday as end time
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

// private IntervalService intervalService;
// private LocationService locationService;
// // 2,3,4,5,6,7,1
// // 0,1,2,3,,4,5,6,7
//
// private static final HashMap<Integer, Integer> dateConversion = new
// HashMap<>();
//
//
// dateConversion.put(2, 0);
// dateConversion.put(3, 1);
// dateConversion.put(4, 2);
// dateConversion.put(5, 3);
// dateConversion.put(6, 6);
// dateConversion.put(7, 5);
//
//

// public void schedule() {
// // get all intervals that fall between mon 0.0.0 to sun 23.59.59
// // excluding repeating int
// rvals where date is not relavant

//
// // get all repeating intervals
// List<Interval> repeatedIntervals = intervalService.getRepeatingIntervals();
// // set the correct date for repeating intervals according to day of week
// (Interval i : repeatedIntervals) {
// // if the repeated interval startdate is later than next week
// // skip it
// i.getStartTime().getTime() > getEndTime().getTime()) {
//

// // convert to correct date
// i.setStartTime(convertDatetime(i.getStartTime(

//

// // get all locations from db

// // run the algo to get assigned intervals
//
// List<Interval> assignedIntervals = Algorithm.processIntervals(intervals,
// locations);
//
//
//
//
//

// Calendar originalDate = Calendar.getInstance();
// originalDate.setTimeInMillis(datetime.getTime());

// // algo runs on sat or sun
// // find the date for upcoming monday
// Calendar updatedDate = Calendar.getInstance();
// e (updatedDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
//

// // add no of days to upcoming monday
// // depneds on day of week of the original date

// String
// pdatedDateStr = dateFormat.format(updatedDate.getTime());

//
// // change the date of original date

//

// // algo runs on sat or sun
// // get upcoming monday as start time
// ate Timestamp getStartTime() {
// Calendar cal = Calendar.getInstance();
// // algo runs on sat or sun
// // find the date for upcoming monday
// e (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
//
//
// cal.set(Calendar.HOUR_OF_DAY, 0);
// cal.set(Calendar.MINUTE, 0);
// cal.set(Calendar.SECOND, 0);
// cal.set(Calendar.MILLISECOND, 0);
//

// // get upcoming sunday as end time
//
// (Calendar.HOUR_OF_DAY,23);
// cal.set(Calendar.MINUTE, 59);
// cal.set(Calendar.SECOND, 59);
// cal.set(Calendar.MILLISECOND, 0);
//
//
//
