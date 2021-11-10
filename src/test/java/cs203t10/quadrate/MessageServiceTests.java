package cs203t10.quadrate;

import cs203t10.quadrate.exception.MessageNotFoundException;
import cs203t10.quadrate.message.Message;
import cs203t10.quadrate.message.MessageRepository;
import cs203t10.quadrate.message.MessageService;
import cs203t10.quadrate.notification.NotificationService;
import cs203t10.quadrate.user.User;
import cs203t10.quadrate.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTests {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MessageService messageService;

    @Test
    void createMessage_NewMessage_ReturnNewMessage() {
        when(messageRepository.save(any(Message.class))).then(returnsFirstArg());
        when(userService.getUser(getSender().getUsername())).thenReturn(getSender());

        Message newMessage = getMessage();
        Message returnedMessage = messageService.createMessage(newMessage);
        assertEquals(newMessage, returnedMessage);

        verify(messageRepository, times(1)).save(newMessage);
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    void getAllMessages_ReturnAllMessages() {
        List<Message> messages = List.of(
                getMessage(),
                getMessage()
        );
        when(messageRepository.findAll()).thenReturn(messages);

        assertEquals(messageService.getAllMessages(), messages);

        verify(messageRepository).findAll();
    }

    @Test
    void getMessage_MessageExist_ReturnMessage() {
        Message message = getMessage();
        when(messageRepository.findById(message.getId())).thenReturn(Optional.of(message));

        assertEquals(messageService.getMessage(message.getId()), message);

        verify(messageRepository).findById(message.getId());
    }

    @Test
    void getMessage_MessageNotExist_ThrowException() {
        when(messageRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () -> messageService.getMessage(100L));

        verify(messageRepository).findById(any(Long.class));
    }

    @Test
    void updateMessage_MessageExist_ReturnUpdatedMessage() {
        Message updatedMessage = getMessage();
        updatedMessage.setSubject("Hi");

        when(messageRepository.findById(updatedMessage.getId())).thenReturn(Optional.of(updatedMessage));
        when(messageRepository.save(updatedMessage)).thenReturn(updatedMessage);
        when(userService.getUser(getSender().getUsername())).thenReturn(getSender());

        Message returnedMessage = messageService.updateMessage(updatedMessage.getId(), updatedMessage);
        assertEquals(returnedMessage, updatedMessage);

        verify(messageRepository).findById(updatedMessage.getId());
        verify(messageRepository).save(updatedMessage);
    }

    @Test
    void updateMessage_MessageNotExist_ThrowException() {
        when(messageRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () -> messageService.updateMessage(100L, any(Message.class)));

        verify(messageRepository).findById(100L);
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    void deleteMessage_MessageExist_ReturnMessage() {
        Message message = getMessage();
        when(messageRepository.findById(message.getId())).thenReturn(Optional.of(message));

        Message deletedMessage = messageService.deleteMessage(message.getId());
        assertEquals(deletedMessage, message);

        verify(messageRepository).findById(message.getId());
        verify(messageRepository,times(1)).deleteById(message.getId());
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    void deleteMessage_MessageNotExist_ThrowException() {
        when(messageRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () -> messageService.deleteMessage(100L));

        verify(messageRepository).findById(any(Long.class));
        verifyNoMoreInteractions(messageRepository);

    }


    private User getSender() {
        return new User("Admin");
    }

    private Message getMessage(){
        Message newMessage = new Message();
        newMessage.setSubject("Hello");
        newMessage.setContent("Welcome to Quadrate!");
        newMessage.setType(1);
        newMessage.setTarget(1);
        newMessage.setUsername("Admin");
        return newMessage;
    }

}


