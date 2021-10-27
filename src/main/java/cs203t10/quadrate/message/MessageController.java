package cs203t10.quadrate.message;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/{username}")
    public Message createMessage(@PathVariable String username, @RequestBody @Valid Message message) {
        return messageService.createMessage(username, message);
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/{id}")
    public Message getMessage(@PathVariable Long id) {
        return messageService.getMessage(id);
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable Long id, @RequestBody @Valid Message message) {
        return messageService.updateMessage(id, message);
    }

    @DeleteMapping("/{id}")
    public Message deleteMessage(@PathVariable Long id) {
        return messageService.deleteMessage(id);
    }
}
