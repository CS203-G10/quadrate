package cs203t10.quadrate.message;

import cs203t10.quadrate.exception.MessageNotFoundException;
import cs203t10.quadrate.exception.UserNotFoundException;
import cs203t10.quadrate.notification.NotificationService;
import cs203t10.quadrate.user.UserRepository;
import cs203t10.quadrate.user.UserService;
import cs203t10.quadrate.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessage(Long id) throws MessageNotFoundException {
        return messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException());
    }

    public Message createMessage(String username, Message message) throws UserNotFoundException {
        return userRepository.findByUsername(username).map(user -> {
            message.setSender(user);
            message.setUpdater(user);
            Message savedMessage = messageRepository.save(message);
            // create notification for target users
            notificationService.createNotification(savedMessage);
            return savedMessage;
        }).orElseThrow(() -> new UserNotFoundException(username));
    }

    public Message updateMessage(Long id, Message message) throws MessageNotFoundException{
        Message messageFound = messageRepository.findById(id).orElseThrow(() -> new MessageNotFoundException());
        messageFound.setSubject(message.getSubject());
        messageFound.setContent(message.getContent());
        messageFound.setUpdateDateTime(new Timestamp(System.currentTimeMillis()));
        // TODO: record updater, need DTO
        messageFound = messageRepository.save(messageFound);

        // mark unread updated notification
        notificationService.markUnread(messageFound);

        return messageFound;
    }

    public Message deleteMessage(Long id) throws MessageNotFoundException{
        Message messageFound = getMessage(id);
        messageRepository.deleteById(id);
        return messageFound;
    }

}
