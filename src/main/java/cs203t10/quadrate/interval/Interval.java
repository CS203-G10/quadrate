package cs203t10.quadrate.interval;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cs203t10.quadrate.location.*;
import cs203t10.quadrate.user.*;
import java.sql.Timestamp;
import java.util.List;

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
    private boolean isRepeated;

    @NotNull
    private Integer priority;

    // is user linked to interval?
    // @JsonBackReference(value = "user")
    // @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // @JsonBackReference(value = "user")
    // @JsonIgnore
    // @ManyToMany(mappedBy = "intervals")
    // @JoinColumn
    // private List<User> attendees;

    // is location linked to interval?
    // @JsonBackReference(value = "location")
    // @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public Interval(Timestamp startTime, Timestamp endTime, String type, boolean isRepeated, Integer priority,
            User user, Location location) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.isRepeated = isRepeated;
        this.priority = priority;
        this.user = user;
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
