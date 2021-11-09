package cs203t10.quadrate.interval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

import cs203t10.quadrate.location.*;

import javax.validation.constraints.NotNull;

@Repository
public interface IntervalRepository extends JpaRepository<Interval, Long> {
        boolean existsById(Long id);

        List<Interval> findAllByStartTimeGreaterThanEqualAndEndTimeIsLessThanEqual(Timestamp startTime,
                        Timestamp endTime);

        List<Interval> findByIsRepeated(Boolean isRepeated);

        @Query("select a from Interval a where type = ?1")
        List<Interval> findByType(String type);

        @Modifying
        @Query("UPDATE Interval SET start_time = :starttime, end_time = :endtime, type = :type, is_repeated = :isRepeated, priority = :priority, user_id = :user_id, location_id = :location_id WHERE id = :id")
        Integer updateInterval(@Param("id") Long id, @Param("starttime") Timestamp starttime,
                        @Param("endtime") Timestamp endtime, @Param("type") String type,
                        @Param("isRepeated") boolean isRepeated, @Param("priority") Integer priority,
                        @Param("user_id") Long userId, @Param("location_id") Long locationId);

        // when adding or updating interval
        // check for any confliction in user schedule(interval)
        // within same type, location and isRepeat
        @Query("SELECT distinct i FROM Interval i JOIN i.attendees u WHERE (i.id <> :id OR :id IS NULL) AND u.id = :userId AND i.type = :type AND i.location = :location AND i.isRepeated = :isRepeated AND ((:starttime >= i.startTime AND :starttime < i.endTime) OR (:endtime > i.startTime AND :endtime <= i.endTime) OR (i.startTime >= :starttime AND i.startTime < :endtime) OR (i.endTime > :starttime AND i.endTime <= :endtime))")
        List<Interval> findConflictedIntervals(@Param("id") Long id, @Param("location") Location location,
                        @Param("starttime") Timestamp startTime, @Param("endtime") Timestamp endTime,
                        @Param("type") String Type, @Param("isRepeated") boolean isRepeated,
                        @Param("userId") Long userId);

        // ensure the newly added starttime and endtime do intercept into existed
        // intervals
        // as well as the existed starttime and endtime do not intercept into newly
        // added interval
        // @Query("SELECT i FROM Interval i WHERE (id <> :id OR :id IS NULL) AND
        // location_id = :location_id AND type = :type AND ((:starttime >= start_time
        // AND :starttime < end_time) OR (:endtime > start_time AND :endtime <=
        // end_time) OR (start_time >= :starttime AND start_time < :endtime) OR
        // (end_time > :starttime AND end_time <= :endtime))")
        // List<Interval> findConflictedIntervals(@Param("location_id") Long locationId,
        // @Param("starttime") Timestamp startTime, @Param("endtime") Timestamp endTime,
        // @Param("type") String Type, @Param("id") Long id);
}
