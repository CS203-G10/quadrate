package cs203t10.quadrate.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id @GeneratedValue
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @PositiveOrZero(message = "Capacity must be zero or positive")
    private Integer capacity;

    @NotNull
    private Boolean bookable;

    @ManyToOne
    @JoinColumn(name = "parent_location_id")
    private Location parentLocation;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<Location> childrenLocation;
}
