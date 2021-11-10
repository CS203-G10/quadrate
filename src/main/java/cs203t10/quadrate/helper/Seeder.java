package cs203t10.quadrate.helper;

import cs203t10.quadrate.interval.Interval;
import cs203t10.quadrate.interval.IntervalService;
import cs203t10.quadrate.location.Location;
import cs203t10.quadrate.location.LocationService;
import cs203t10.quadrate.message.Message;
import cs203t10.quadrate.message.MessageService;
import cs203t10.quadrate.user.User;
import cs203t10.quadrate.user.UserService;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Seeder {
    private final UserService userService;
    private final IntervalService intervalService;
    private final LocationService locationService;
    private final MessageService messageService;

    @EventListener
    @Order(0)
    public void createGenesisOnStart(ApplicationStartedEvent event) {
        // create users
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("password");
        admin.setRole("ROLE_ADMIN");
        userService.createUser(admin);

        User yx = new User();
        yx.setUsername("Yu Xuan");
        yx.setPassword("password");
        yx.setRole("ROLE_USER");
        yx.setCredit(10);
        userService.createUser(yx);

        User joy = new User();
        joy.setUsername("Joy");
        joy.setPassword("password");
        joy.setRole("ROLE_User");
        joy.setCredit(10);
        userService.createUser(joy);

        User fz = new User();
        fz.setUsername("Fang Zhou");
        fz.setPassword("password");
        fz.setRole("ROLE_User");
        fz.setCredit(10);
        userService.createUser(fz);

        // create location
        Location office = new Location("Office", 2, true);
        locationService.createLocation(office);

        Location mtRoom1 = new Location("Meeting Room 1", 2, true);
        mtRoom1.setParentLocation(office);
        locationService.createLocation(mtRoom1);

        Location mtRoom2 = new Location("Meeting Room 2", 2, true);
        mtRoom2.setParentLocation(office);
        locationService.createLocation(mtRoom2);

        // create intervals
        Calendar start = Calendar.getInstance();
        start.set(2021, 10, 17);
        start.set(Calendar.HOUR_OF_DAY, 12);
        start.set(Calendar.MINUTE, 30);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(2021, 10, 17);
        end.set(Calendar.HOUR_OF_DAY, 16);
        end.set(Calendar.MINUTE, 30);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        // success - first interval
        Interval int1 = new Interval(new Timestamp(start.getTimeInMillis()), new Timestamp(end.getTimeInMillis()),
                "Preference", false, 1, yx, Set.of(yx, joy), mtRoom1);
        intervalService.createInterval(int1);

        // fail - same time diff location as int1, exits parent location capacity
        Interval int2 = new Interval(new Timestamp(start.getTimeInMillis()), new Timestamp(end.getTimeInMillis()),
                "Preference", false, 1, fz, Set.of(fz), mtRoom2);
        intervalService.createInterval(int2);

        start.set(2021, 10, 18);
        end.set(2021, 10, 18);

        // success - diff time same location as int1, no conflict
        Interval int3 = new Interval(new Timestamp(start.getTimeInMillis()), new Timestamp(end.getTimeInMillis()),
                "Preference", false, 1, fz, Set.of(fz), mtRoom1);
        intervalService.createInterval(int3);

        start.set(Calendar.HOUR_OF_DAY, 16);
        end.set(Calendar.HOUR_OF_DAY, 18);

        // success - diff time same location as int3, repeated, no conflict
        Interval int4 = new Interval(new Timestamp(start.getTimeInMillis()), new Timestamp(end.getTimeInMillis()),
                "Preference", true, 1, fz, Set.of(fz), mtRoom1);
        intervalService.createInterval(int4);

         //messages
        Message message1 = new Message();
        message1.setSubject("Hello");
        message1.setContent("Welcome to Quadrate.");
        message1.setTarget(2);
        message1.setType(1);
        message1.setUsername("admin");
        messageService.createMessage(message1);

        Message message2 = new Message();
        message2.setSubject("Hello users");
        message2.setContent("This is an announcement.");
        message2.setTarget(2);
        message2.setType(2);
        message2.setUsername("admin");
        messageService.createMessage(message2);

        Message message3 = new Message();
        message3.setSubject("Hello users");
        message3.setContent("This is a news.");
        message3.setTarget(2);
        message3.setType(2);
        message3.setUsername("admin");
        messageService.createMessage(message3);

        Message message4 = new Message();
        message4.setSubject("Hello users");
        message4.setContent("This is a notification.");
        message4.setTarget(2);
        message4.setType(1);
        message4.setUsername("admin");
        messageService.createMessage(message4);

    }
}