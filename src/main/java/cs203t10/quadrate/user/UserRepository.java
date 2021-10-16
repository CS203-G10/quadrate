package cs203t10.quadrate.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Integer deleteByUsername(String username);

    @Modifying
    @Query("UPDATE User SET username = ?2, password = ?3, role = ?4 WHERE username = ?1")
    Integer updateUser(String username, String newUsername, String newPassword, String newRole);
}
