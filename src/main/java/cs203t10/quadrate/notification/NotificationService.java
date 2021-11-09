package cs203t10.quadrate.notification;

import cs203t10.quadrate.exception.NotificationNotFoundException;
import cs203t10.quadrate.message.Message;
import cs203t10.quadrate.user.User;
import cs203t10.quadrate.user.UserRepository;
import cs203t10.quadrate.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public void createNotification(Message message){
        List <User> users = getTargets(message.getTarget());
        for(User user:users){
            notificationRepository.save(new Notification(user, message));
        }
    }

    public List<User> getTargets(int target){
        List<User> users = userService.getAllUsers();

        // target 2: all users
        if(target == 2) {
            return users;
        }
        // target 1: admins only
        for(User user:users){
            if(user.getRole().equals("ROLE_USER")){
                users.remove(user);
            }
        }
        return users;
    }

    public List<Notification> getAllNotifications(String username){
        User user = userService.getUser(username);
        return notificationRepository.findByRecipientOrderByReadAsc(user);
    }


    public Notification getNotification(Long notificationId) throws NotificationNotFoundException {
        return notificationRepository.findById(notificationId).orElseThrow(() -> new NotificationNotFoundException());
    }


    public Notification readNotification(Long notificationId) throws NotificationNotFoundException {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotificationNotFoundException());
        notification.setRead(true);

        return notificationRepository.save(notification);
    }

    public void markUnread(Message message){
            List <Notification> notificationList = notificationRepository.findByMessage(message);
            for(Notification notification: notificationList){
                notification.setRead(false);
                notificationRepository.save(notification);
        }
    }
//
//    @Override
//    public Integer countUnread(Long userId){
//        return notificationRepository.countByRecipientIdAndReadFalse(userId);
//    }
}
