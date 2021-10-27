package cs203t10.quadrate.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cs203t10.quadrate.message.Message;
import cs203t10.quadrate.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    private Boolean read = false;

    public Notification(User recipient, Message message) {
        this.recipient = recipient;
        this.message = message;
    }
}
