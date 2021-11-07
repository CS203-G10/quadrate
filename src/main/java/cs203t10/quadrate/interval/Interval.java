package cs203t10.quadrate.interval;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
// import com.fasterxml.jackson.annotation.JsonBackReference;
// import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.util.*;

import cs203t10.quadrate.location.*;
import cs203t10.quadrate.user.*;

@Entity
@Table(name = "interval_table")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Interval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ZZZZ")
    private Timestamp startTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss ZZZZ")
    private Timestamp endTime;

    // maybe can add something similar to user ROLE instead of string input
    @NotBlank(message = "Type cannot be blank")
    private String type;

    @NotNull
    private Boolean isRepeated;

    @NotNull
    private Integer priority;

    // @JsonBackReference(value = "user")
    // @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    // // @JsonBackReference(value = "user")
    // @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_interval", joinColumns = @JoinColumn(name = "interval_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> attendees = new HashSet<>();

    // @JsonBackReference(value = "location")
    // @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public Interval(Timestamp startTime, Timestamp endTime, String type, Boolean isRepeated, Integer priority,
            User creator, Set<User> attendees, Location location) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.isRepeated = isRepeated;
        this.priority = priority;
        this.creator = creator;
        this.attendees = attendees;
        this.location = location;
    }

    // public Interval(Timestamp startTime, Timestamp endTime, String type, boolean
    // isRepeated, Integer priority,
    // User user, List<User> attendees, Location location) {
    // this.startTime = startTime;
    // this.endTime = endTime;
    // this.type = type;
    // this.isRepeated = isRepeated;
    // this.priority = priority;
    // this.user = user;
    // this.attendees = attendees;
    // this.location = location;
    // }
}
