package cs203t10.quadrate.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import cs203t10.quadrate.interval.Interval;
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

    // @JsonManagedReference(value = "user")
    // @JsonBackReference
    // @JsonIgnore
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Interval> intervals;

    public User(String username) {
        this.username = username;
    }

    public User(Long id, String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
