package cs203t10.quadrate.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import cs203t10.quadrate.interval.Interval;
// import com.fasterxml.jackson.annotation.JsonManagedReference;
// import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @PositiveOrZero(message = "Capacity must be zero or positive")
    private Integer capacity;

    @NotNull
    private Boolean bookable;

    // @JsonBackReference(value = "location_parant_child")
    // @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_location_id")
    private Location parentLocation;

    // @JsonManagedReference(value = "location_parant_child")
    @JsonIgnore
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<Location> childrenLocation;

    // @JsonBackReference(value = "location")
    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<Interval> intervals;

    // added by yuxaun as i need it for intervalServiceTest
    public Location(String name, Integer capacity, Boolean bookable) {
        this.name = name;
        this.capacity = capacity;
        this.bookable = bookable;
    }
}
