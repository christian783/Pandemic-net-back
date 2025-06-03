package com.app.pandemicnet.repository;

import com.app.pandemicnet.model.Entry;
import com.app.pandemicnet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    @Query("SELECT e FROM Entry e WHERE e.user.id = :userId AND e.timestamp >= :startTime")
    List<Entry> findByUserIdAndTimeRange(Long userId, LocalDateTime startTime);

    List<Entry> findByUserId(Long userId);

    @Query("SELECT DISTINCT e.user FROM Entry e WHERE e.dataCentre.id = :dataCentreId " +
            "AND e.timestamp BETWEEN :startTime AND :endTime AND e.user.id != :excludeUserId")
    List<User> findAtRiskUsers(Long dataCentreId, LocalDateTime startTime, LocalDateTime endTime, Long excludeUserId);

    @Query("select e from Entry e where e.user.id = :userId and e.timestamp between :startTime and :endTime")
    List<Entry> findEntriesByUserIdAndTimestampBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("select e from Entry e where e.user.id = :user_id ")
    List<Entry> findEntriesByUserId(Long user_id);

    @Query("SELECT e FROM Entry e WHERE e.user.id = :userId AND e.timestamp BETWEEN :start AND :end")
    List<Entry> findByUserIdAndTimestampBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query(value = "SELECT e1.user_id, e2.user_id, " +
            "MIN(ABS(TIMESTAMPDIFF(SECOND, e1.timestamp, e2.timestamp))), " +
            "e1.data_centre_id, e1.timestamp " +
            "FROM entries e1 " +
            "JOIN entries e2 ON e1.data_centre_id = e2.data_centre_id " +
            "AND e1.user_id < e2.user_id " +
            "AND e1.user_id = :userId " +
            "AND ABS(TIMESTAMPDIFF(SECOND, e1.timestamp, e2.timestamp)) <= 14 * 24 * 60 * 60 " +
            "GROUP BY e1.user_id, e2.user_id, e1.data_centre_id, e1.timestamp", nativeQuery = true)
    List<Object[]> findUserInteractions(@Param("userId") Long userId);

}