package cs203t10.quadrate.interval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface IntervalRepository extends JpaRepository<Interval, Long> {
    @Modifying
    @Query("UPDATE Interval SET start_time = :starttime, end_time = :endtime, type = :type, user_id = :user_id, location_id = :location_id WHERE id = :id")
    Integer updateInterval(@Param("id") Long id, @Param("starttime") Timestamp starttime,
            @Param("endtime") Timestamp endtime, @Param("type") String type, @Param("user_id") Long userId,
            @Param("location_id") Long locationId);

    // @Query(value = "Select * FROM interval_table i WHERE i.id <> ?5 AND
    // i.location_id = ?1 AND i.type = ?4 AND (i.end_time > ?2 OR i.start_time <
    // ?3)", nativeQuery = true)
    // @Query("SELECT i FROM Interval i WHERE id != :id AND location_id =
    // :location_id AND type = :type AND end_time > :starttime OR start_time <
    // :endtime")

    // ensure the newly added starttime and endtime do intercept into existed
    // intervals
    // as well as the existed starttime and endtime do not intercept into newly
    // added interval
    @Query("SELECT i FROM Interval i WHERE id != :id AND location_id = :location_id AND type = :type AND ((:starttime >= start_time AND :starttime < end_time) OR (:endtime > start_time AND :endtime <= end_time) OR (start_time >= :starttime AND start_time < :endtime) OR (end_time > :starttime AND end_time <= :endtime))")
    List<Interval> findConflictedIntervals(@Param("location_id") Long locationId,
            @Param("starttime") Timestamp startTime, @Param("endtime") Timestamp endTime, @Param("type") String Type,
            @Param("id") Long id);
}
