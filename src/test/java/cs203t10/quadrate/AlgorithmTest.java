package cs203t10.quadrate;

import cs203t10.quadrate.algorithm.*;
import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.location.Location;
import cs203t10.quadrate.user.User;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlgorithmTest {
    private final User creator = new User("YuXuan");
    private final Set<User> attendees = new HashSet<>();
    private final Location bookedLocation = new Location("Meeting Room", 1, true);
    private final Timestamp startTime;
    private final Timestamp endTime;
    {
        attendees.add(new User("YuXuan"));
        attendees.add(new User("Yuki"));
        bookedLocation.setId(1L);

        // start time
        Calendar cal = Calendar.getInstance();
        cal.set(2021, 10, 21);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        startTime = new Timestamp(cal.getTimeInMillis());

        // end time
        cal = Calendar.getInstance();
        cal.set(2021, 10, 21);
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        endTime = new Timestamp(cal.getTimeInMillis());
    }

    private Scheduler s = new Scheduler();

    @Test
    void AddOneIntervalWithOneAttendee_WithinCurrentLocationCapacity_ReturnAllIntervals() {
        System.out.println("------------------------------test 1------------------------------");
        // s.schedule();
        // Timestamp.valueOf(LocalDateTime.of())
        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, bookedLocation);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);

        List<Interval> approvedIntervals = Algorithm.processIntervals(List.of(int1), List.of(bookedLocation));

        // interval.setType("assigned");

        Interval expected1 = new Interval(startTime, endTime, "assigned", true, 1, creator, int1.getAttendees(),
                int1.getLocation());

        intervals.clear();
        intervals.add(expected1);

        assertResults(intervals, approvedIntervals);
    }

    @Test
    void AddOneIntervalWithTwoAttendees_ExitsCurrentLocationCapacity_ReturnEmptyList() {
        System.out.println("------------------------------test 2------------------------------");
        // Timestamp.valueOf(LocalDateTime.of())

        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));
        attendees1.add(new User("Yuki"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, bookedLocation);

        // when(algorithmHelper.getApprovedIntervals()).thenReturn(List.of(interval));

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);

        List<Interval> approvedIntervals = Algorithm.processIntervals(List.of(int1), List.of(bookedLocation));

        // interval.setType("assigned");

        Interval expected1 = new Interval(startTime, endTime, "assigned", true, 1, creator, int1.getAttendees(),
                int1.getLocation());

        intervals.clear();
        intervals.add(expected1);

        assertResults(intervals, approvedIntervals);
    }

    @Test
    void AddTwoIntervalsSameLocation_WithinCurrentLocationCapacity_ReturnAllIntervals() {
        System.out.println("------------------------------test 3------------------------------");

        Location office = new Location("Office", 5, true);
        office.setId(1L);
        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));
        attendees1.add(new User("Yuki"));

        Set<User> attendees2 = new HashSet<>();
        attendees2.add(new User("Joy"));
        attendees2.add(new User("FZ"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, office);
        Interval int2 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees2, office);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);
        intervals.add(int2);

        List<Interval> approvedIntervals = Algorithm.processIntervals(intervals, List.of(office));

        // int1.setType("assigned");
        // int2.setType("assigned");

        Interval expected1 = new Interval(startTime, endTime, "assigned", true, 1, creator, attendees1, office);
        Interval expected2 = new Interval(startTime, endTime, "assigned", true, 1, creator, attendees2, office);

        intervals.clear();
        intervals.add(expected1);
        intervals.add(expected2);

        assertResults(intervals, approvedIntervals);
    }

    @Test
    void AddTwoIntervalsSameLocation_ExistsCurrentLocationCapacity_ReturnFirstIntervalAdded() {
        System.out.println("------------------------------test 4------------------------------");

        Location office = new Location("Office", 5, true);
        office.setId(1L);
        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));
        attendees1.add(new User("Yuki"));

        Set<User> attendees2 = new HashSet<>();
        attendees2.add(new User("Joy"));
        attendees2.add(new User("FZ"));
        attendees2.add(new User("R"));
        attendees2.add(new User("C"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, office);
        Interval int2 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees2, office);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);
        intervals.add(int2);

        List<Interval> approvedIntervals = Algorithm.processIntervals(intervals, List.of(office));

        // int1.setType("assigned");

        Interval expected1 = new Interval(startTime, endTime, "assigned", true, 1, creator, attendees1, office);

        intervals.clear();
        intervals.add(expected1);

        assertResults(intervals, approvedIntervals);
    }

    @Test
    void AddTwoIntervalsSameLocation_WithinParentLocationCapacity_ReturnAllIntervals() {
        System.out.println("------------------------------test 5------------------------------");

        Location office = new Location("Office", 5, true);
        office.setId(1L);
        Location mtRoom = new Location("Meeting Room", 5, true);
        mtRoom.setId(2L);
        mtRoom.setParentLocation(office);

        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));
        attendees1.add(new User("Yuki"));

        Set<User> attendees2 = new HashSet<>();
        attendees2.add(new User("Joy"));
        attendees2.add(new User("FZ"));
        attendees2.add(new User("R"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, mtRoom);
        Interval int2 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees2, mtRoom);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);
        intervals.add(int2);

        List<Interval> approvedIntervals = Algorithm.processIntervals(intervals, List.of(office, mtRoom));

        // int1.setType("assigned");
        // int2.setType("assigned");

        Interval expected1 = new Interval(startTime, endTime, "assigned", true, 1, creator, attendees1, mtRoom);
        Interval expected2 = new Interval(startTime, endTime, "assigned", true, 1, creator, attendees2, mtRoom);

        intervals.clear();
        intervals.add(expected1);
        intervals.add(expected2);

        assertResults(intervals, approvedIntervals);
    }

    @Test
    void AddTwoIntervalsSameLocation_ExistsParentLocationCapacity_ReturnFirstIntervalAdded() {
        System.out.println("------------------------------test 6------------------------------");

        Location office = new Location("Office", 3, true);
        office.setId(1L);
        Location mtRoom = new Location("Meeting Room", 5, true);
        mtRoom.setId(2L);
        mtRoom.setParentLocation(office);

        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));
        attendees1.add(new User("Yuki"));

        Set<User> attendees2 = new HashSet<>();
        attendees2.add(new User("Joy"));
        attendees2.add(new User("FZ"));
        attendees2.add(new User("R"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, mtRoom);
        Interval int2 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees2, mtRoom);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);
        intervals.add(int2);

        List<Interval> approvedIntervals = Algorithm.processIntervals(intervals, List.of(office, mtRoom));

        // int1.setType("assigned");

        Interval expected1 = new Interval(startTime, endTime, "assigned", true, 1, creator, attendees1, mtRoom);

        intervals.clear();
        intervals.add(expected1);

        assertResults(intervals, approvedIntervals);
    }

    @Test
    void AddTwoIntervalsDifferentLocation_WithinParentLocationCapacity_ReturnAllIntervals() {
        System.out.println("------------------------------test 7------------------------------");

        Location office = new Location("Office", 5, true);
        office.setId(1L);
        Location mtRoom1 = new Location("Meeting Room 1", 2, true);
        mtRoom1.setId(2L);
        mtRoom1.setParentLocation(office);
        Location mtRoom2 = new Location("Meeting Room 2", 3, true);
        mtRoom2.setId(3L);
        mtRoom2.setParentLocation(office);

        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));
        attendees1.add(new User("Yuki"));

        Set<User> attendees2 = new HashSet<>();
        attendees2.add(new User("Joy"));
        attendees2.add(new User("FZ"));
        attendees2.add(new User("R"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, mtRoom1);
        Interval int2 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees2, mtRoom2);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);
        intervals.add(int2);

        List<Interval> approvedIntervals = Algorithm.processIntervals(intervals, List.of(office, mtRoom1, mtRoom2));

        // int1.setType("assigned");
        // int2.setType("assigned");

        Interval expected1 = new Interval(startTime, endTime, "assigned", true, 1, creator, attendees1, mtRoom1);
        Interval expected2 = new Interval(startTime, endTime, "assigned", true, 1, creator, attendees2, mtRoom2);

        intervals.clear();
        intervals.add(expected1);
        intervals.add(expected2);

        assertResults(intervals, approvedIntervals);
    }

    @Test
    void AddTwoIntervalsDifferentLocation_ExitsParentLocationCapacity_ReturnFirstIntervalAdded() {
        System.out.println("------------------------------test 8------------------------------");

        Location office = new Location("Office", 3, true);
        office.setId(1L);
        Location mtRoom1 = new Location("Meeting Room 1", 2, true);
        mtRoom1.setId(2L);
        mtRoom1.setParentLocation(office);
        Location mtRoom2 = new Location("Meeting Room 2", 3, true);
        mtRoom2.setId(3L);
        mtRoom2.setParentLocation(office);

        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));
        attendees1.add(new User("Yuki"));

        Set<User> attendees2 = new HashSet<>();
        attendees2.add(new User("Joy"));
        attendees2.add(new User("FZ"));
        attendees2.add(new User("R"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, mtRoom1);
        Interval int2 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees2, mtRoom2);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);
        intervals.add(int2);

        List<Interval> approvedIntervals = Algorithm.processIntervals(intervals, List.of(office, mtRoom1, mtRoom2));

        // int1.setType("assigned");

        Interval expected = new Interval(startTime, endTime, "assigned", true, 1, creator, attendees1, mtRoom1);

        intervals.clear();
        intervals.add(expected);

        assertResults(intervals, approvedIntervals);
    }

    @Test
    void AddTwoIntervalsWithDiffPriority_ExitsParentLocationCapacity_ReturnIntervalWithHigherPriority() {
        System.out.println("------------------------------test 9------------------------------");

        Location office = new Location("Office", 3, true);
        office.setId(1L);
        Location mtRoom1 = new Location("Meeting Room 1", 2, true);
        mtRoom1.setId(2L);
        mtRoom1.setParentLocation(office);
        Location mtRoom2 = new Location("Meeting Room 2", 3, true);
        mtRoom2.setId(3L);
        mtRoom2.setParentLocation(office);

        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));
        attendees1.add(new User("Yuki"));

        Set<User> attendees2 = new HashSet<>();
        attendees2.add(new User("Joy"));
        attendees2.add(new User("FZ"));
        attendees2.add(new User("R"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, mtRoom1);
        Interval int2 = new Interval(startTime, endTime, "Preference", true, 2, creator, attendees2, mtRoom2);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);
        intervals.add(int2);

        List<Interval> approvedIntervals = Algorithm.processIntervals(intervals, List.of(office, mtRoom1, mtRoom2));

        // int2.setType("assigned");

        Interval expected = new Interval(startTime, endTime, "assigned", true, 2, creator, attendees2, mtRoom2);

        intervals.clear();
        intervals.add(expected);

        assertResults(intervals, approvedIntervals);
    }

    @Test
    void AddTwoIntervalsWithSamePriorityDiffTiming_ExitsParentLocationCapacity_ReturnIntervalWithHigherPriority() {
        System.out.println("------------------------------test 10------------------------------");

        Location office = new Location("Office", 3, true);
        office.setId(1L);
        Location mtRoom1 = new Location("Meeting Room 1", 2, true);
        mtRoom1.setId(2L);
        mtRoom1.setParentLocation(office);
        Location mtRoom2 = new Location("Meeting Room 2", 3, true);
        mtRoom2.setId(3L);
        mtRoom2.setParentLocation(office);

        Set<User> attendees1 = new HashSet<>();
        attendees1.add(new User("YuXuan"));
        attendees1.add(new User("Yuki"));

        Set<User> attendees2 = new HashSet<>();
        attendees2.add(new User("Joy"));
        attendees2.add(new User("FZ"));
        attendees2.add(new User("R"));

        Interval int1 = new Interval(startTime, endTime, "Preference", true, 1, creator, attendees1, mtRoom1);
        // adding another hour to end time for int2
        long t = endTime.getTime();
        long m = 1 * 60 * 60 * 1000;
        Timestamp et = new Timestamp(t + m);
        Interval int2 = new Interval(startTime, et, "Preference", true, 1, creator, attendees2, mtRoom2);

        List<Interval> intervals = new ArrayList<>();
        intervals.add(int1);
        intervals.add(int2);

        List<Interval> approvedIntervals = Algorithm.processIntervals(intervals, List.of(office, mtRoom1, mtRoom2));

        Interval expected = new Interval(startTime, et, "assigned", true, 1, creator, attendees2, mtRoom2);

        intervals.clear();
        intervals.add(expected);

        assertResults(intervals, approvedIntervals);
    }

    private void assertResults(List<Interval> expected, List<Interval> results) {
        for (int i = 0; i < results.size(); i++) {
            assertEquals(expected.get(i).getStartTime(), results.get(i).getStartTime());
            assertEquals(expected.get(i).getEndTime(), results.get(i).getEndTime());
            assertEquals(expected.get(i).getType(), results.get(i).getType());
            assertEquals(expected.get(i).getIsRepeated(), results.get(i).getIsRepeated());
            assertEquals(expected.get(i).getPriority(), results.get(i).getPriority());
            assertEquals(expected.get(i).getCreator(), results.get(i).getCreator());
            assertEquals(expected.get(i).getAttendees(), results.get(i).getAttendees());
            assertEquals(expected.get(i).getLocation(), results.get(i).getLocation());
        }
    }

    // diff timing same location does it pass?

    // private Timestamp startTime {
    // Calendar cal = Calendar.getInstance();
    // cal.set(2021, 10, 21);
    // cal.set(Calendar.HOUR_OF_DAY, 12);
    // cal.set(Calendar.MINUTE, 30);
    // cal.set(Calendar.SECOND, 0);
    // cal.set(Calendar.MILLISECOND, 0);
    // return new Timestamp(cal.getTimeInMillis());
    // }

    // private Timestamp endTime {
    // Calendar cal = Calendar.getInstance();
    // cal.set(2021, 10, 21);
    // cal.set(Calendar.HOUR_OF_DAY, 16);
    // cal.set(Calendar.MINUTE, 30);
    // cal.set(Calendar.SECOND, 0);
    // cal.set(Calendar.MILLISECOND, 0);
    // return new Timestamp(cal.getTimeInMillis());
    // }

    // private User creator {
    // return new User("YuXuan");
    // }

    // private Location bookedLocation {
    // Location location = new Location("Meeting Room", 1, true);
    // location.setId(1L);
    // return location;
    // }

    // private Set<User> getAttendees() {
    // Set<User> urs = new HashSet<>();
    // urs.add(new User("YuXuan"));
    // urs.add(new User("Yuki"));
    // return urs;
    // }
}
