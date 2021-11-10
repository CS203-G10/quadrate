package cs203t10.quadrate.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(/*origins ="http://localhost:3000"*/ "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/inbox/{username}")
    public List<Notification> getAllNotifications(@PathVariable String username) {
        return notificationService.getAllNotifications(username);
    }

//    @GetMapping("/unread/{userId}")
//    public Integer countUnread(@PathVariable Long userId) {
//        return notificationService.countUnread(userId);
//    }

    @GetMapping("/{id}")
    public Notification getNotification(@PathVariable Long id) {
        return notificationService.getNotification(id);
    }

    @PutMapping("/{id}")
    public Notification readNotification(@PathVariable Long id) {
        return notificationService.readNotification(id);
    }

}
