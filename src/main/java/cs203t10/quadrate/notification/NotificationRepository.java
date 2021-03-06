package cs203t10.quadrate.notification;

import cs203t10.quadrate.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cs203t10.quadrate.user.User;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByReadAsc(User user);
    List<Notification> findByMessage(Message message);
//    Integer countByRecipientAndReadFalse(Long recipientId);
}
