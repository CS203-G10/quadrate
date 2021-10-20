package cs203t10.quadrate.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(columnList = "username", unique = true))
public class User {
    @Id @GeneratedValue
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Length(message = "Password must be at least 8 characters in length", min = 8)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    private String role;

    public User(String username) {
        this.username = username;
    }
}
