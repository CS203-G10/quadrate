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
public class Interval implements Cloneable {
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

    public Long getPriorityScore() {
        return (endTime.getTime() - startTime.getTime()) * priority;
    }

    public Long start(){
        return startTime.getTime();
    }
    public Long end(){
        return endTime.getTime();
    }

    public Long length() {
        return endTime.getTime() - startTime.getTime();
    }

    /**
     * Returns if this interval is adjacent to the specified interval.
     * <p>
     * Two intervals are adjacent if either one ends where the other starts.
     * @param interval - the interval to compare this one to
     * @return if this interval is adjacent to the specified interval.
     */
    public boolean isAdjacent(Interval other) {
        return start() == other.end() || end() == other.start();
    }

    public boolean overlaps(Interval o) {
        return end() > o.start() && o.end() > start();
    }

    public int compareTo(Interval o) {
        if (start() > o.start()) {
            return 1;
        } else if (start() < o.start()) {
            return -1;
        } else if (end() > o.end()) {
            return 1;
        } else if (end() < o.end()) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public Interval clone() {
        try {
            Interval clone = (Interval) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            clone.setStartTime(startTime);
            clone.setEndTime(endTime);
            clone.setType(type);
            clone.setIsRepeated(isRepeated);
            clone.setPriority(priority);
            clone.setCreator(creator);
            clone.setAttendees(attendees);
            clone.setLocation(location);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
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
