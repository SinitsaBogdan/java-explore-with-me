package ru.practicum.repo;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class EndpointRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EndpointRepository statsRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void createTestData() {
        createEndpointHit("app1", "/uri1", "127.0.0.1", LocalDateTime.now());
        createEndpointHit("app1", "/uri1", "127.0.0.1", LocalDateTime.now());
        createEndpointHit("app1", "/uri1", "127.0.0.2", LocalDateTime.now());
        createEndpointHit("app1", "/uri1", "127.0.0.2", LocalDateTime.now());
        createEndpointHit("app1", "/uri1", "127.0.0.3", LocalDateTime.now());
        createEndpointHit("app1", "/uri1", "127.0.0.3", LocalDateTime.now());
        createEndpointHit("app1", "/uri2", "127.0.0.3", LocalDateTime.now());
        createEndpointHit("app1", "/uri2", "127.0.0.3", LocalDateTime.now());
        createEndpointHit("app1", "/uri1", "127.0.0.3", LocalDateTime.now().minusMonths(1));
        createEndpointHit("app1", "/uri1", "127.0.0.3", LocalDateTime.now().minusMonths(1));
        createEndpointHit("app1", "/uri2", "127.0.0.3", LocalDateTime.now().minusMonths(1));
        createEndpointHit("app1", "/uri2", "127.0.0.3", LocalDateTime.now().minusMonths(1));

        assertThat(statsRepository.findAll()).size().isEqualTo(12);
    }

    private void createEndpointHit(String app, String uri, String ip, LocalDateTime timestamp) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(app);
        endpointHit.setUri(uri);
        endpointHit.setIp(ip);
        endpointHit.setHitTimestamp(timestamp);

        testEntityManager.persist(endpointHit);
    }
}