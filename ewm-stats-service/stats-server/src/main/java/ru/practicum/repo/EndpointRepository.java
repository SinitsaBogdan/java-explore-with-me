package ru.practicum.repo;

import ru.practicum.dto.ViewEndpointDto;
import ru.practicum.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.dto.ViewEndpointDto(h.app AS app, h.uri AS uri, COUNT(h.uri) AS hits) " +
            "FROM EndpointHit AS h " +
            "GROUP BY h.app, h.uri "
//            "WHERE h.timestamp BETWEEN (:start) AND (:end) " +
//            "AND (COALESCE(:uris, null) IS NULL OR h.uri IN (:uris)) " +
//            "GROUP BY h.app, h.uri " +
//            "ORDER BY hits DESC "
    )
    List<ViewEndpointDto> findEndpoint(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.dto.ViewEndpointDto(h.app AS app, h.uri AS uri, COUNT(distinct h.ip) AS hits) " +
            "FROM EndpointHit AS h " +
            "GROUP BY h.app, h.uri "

//            "WHERE h.timestamp BETWEEN (:start) AND (:end) " +
//            "AND (COALESCE(:uris, null) IS NULL OR h.uri IN (:uris)) " +
//            "GROUP BY h.app, h.uri " +
//            "ORDER BY hits DESC "
    )
    List<ViewEndpointDto> findUniqueEndpoint(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);
}