package cs203t10.quadrate.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cs203t10.quadrate.notification.Notification;
import cs203t10.quadrate.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject cannot be blank")
    private String subject;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @CreationTimestamp
    private Timestamp publishDateTime;

    @CreationTimestamp
    private Timestamp updateDateTime;

    @NotNull
    private int target;

    @NotNull
    private int type;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "updater_id")
    private User updater;

    @JsonIgnore
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

//    public Message(String subject, String content, int target, int type){
//        this.subject = subject;
//        this.content = content;
//        this.target = target;
//        this.type = type;
//    }
}
