package ru.practicum.repo;

import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointRepository extends JpaRepository<EndpointHit, Long> {

    @Query(
            "SELECT e.app AS app, e.uri AS uri, COUNT(DISTINCT e.ip) AS hits " +
            "FROM EndpointHit e " +
            "WHERE e.hitTimestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR e.uri IN :uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY hits DESC"
    )
    List<ViewStatsProjection> findUniqueStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query(
            "SELECT e.app AS app, e.uri AS uri, COUNT(e.ip) AS hits " +
            "FROM EndpointHit  e " +
            "WHERE e.hitTimestamp BETWEEN :start AND :end " +
            "AND ((:uris) IS NULL OR e.uri IN :uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY hits DESC"
    )
    List<ViewStatsProjection> findNotUniqueStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );
}