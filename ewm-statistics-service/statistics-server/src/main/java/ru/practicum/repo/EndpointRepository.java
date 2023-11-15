package ru.practicum.repo;

import ru.practicum.dto.ViewEndpointDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN (:start) AND (:end) " +
            "AND (COALESCE(:uris, null) IS NULL OR h.uri IN (:uris)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY 3 DESC ")
    List<ViewEndpointDto> findEndpoint(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(h.app, h.uri, COUNT(distinct h.ip)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN (:start) AND (:end) " +
            "AND (COALESCE(:uris, null) IS NULL OR h.uri IN (:uris)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY 3 DESC ")
    List<ViewEndpointDto> findUniqueEndpoint(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

}
