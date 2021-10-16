package cs203t10.quadrate.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Modifying
    @Query("UPDATE Location SET name = :name, capacity = :capacity, bookable = :bookable, parentLocation = :parent_id WHERE id = :id")
    Integer updateLocation(@Param("id") Long id,
                           @Param("name") String name,
                           @Param("capacity") Integer capacity,
                           @Param("bookable") Boolean bookable,
                           @Param("parent_id") Long parentLocationId);
}
