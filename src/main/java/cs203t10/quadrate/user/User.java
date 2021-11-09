package cs203t10.quadrate.user;

import cs203t10.quadrate.message.Message;
import cs203t10.quadrate.notification.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;
import cs203t10.quadrate.interval.Interval;
// import cs203t10.quadrate.userinterval.*;
// import com.fasterxml.jackson.annotation.JsonManagedReference;
// import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(columnList = "username", unique = true))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Length(message = "Password must be at least 8 characters in length", min = 8)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    private String role;

    // not sure is the data type ok
    private Double minHr;

    private Integer priority;

    // @JsonManagedReference(value = "user")
    // @JsonBackReference
    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private List<Interval> createdIntervals;

    @JsonIgnore
    @ManyToMany(mappedBy = "attendees", cascade = CascadeType.ALL)
    private Set<Interval> attendedIntervals = new HashSet<>();

    // @JsonIgnore
    // @ManyToMany(mappedBy = "user")
    // private List<UserInterval> userIntervals;

    // @JsonIgnore
    // @ManyToMany(mappedBy = "user")
    // private List<Interval> attendedIntervals;

    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @PreRemove
    private void preRemove() {
        for (Message message : sentMessages) {
            message.setSender(null);
        }
    }

    public User(String username) {
        this.username = username;
    }

    public User(Long id, String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String username, String password, String role, Integer priority) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.priority = priority;
    }
}
