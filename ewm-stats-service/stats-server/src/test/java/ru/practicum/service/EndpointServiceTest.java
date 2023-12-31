package ru.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewEndpointDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.repo.EndpointRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EndpointServiceTest {
    @InjectMocks
    private EndpointServiceImpl service;

    @Mock
    private EndpointRepository repository;

//    @Test
//    void getStats() {
//        when(repository.findEndpoint(any(), any(), any())).thenReturn(getListViewEndpointDto());
//        List<ViewEndpointDto> result = service.getStats(
//                LocalDateTime.of(2020, 10, 1, 10, 0),
//                LocalDateTime.of(2023, 10, 1, 10, 0),
//                new ArrayList<>(), false
//        );
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(result.size(), 2);
//    }

//    @Test
//    void getStats_Unique() {
//        when(repository.findUniqueEndpoint(any(), any(), any())).thenReturn(getListViewEndpointDto());
//        List<ViewEndpointDto> result = service.getStats(
//                LocalDateTime.of(2020, 10, 1, 10, 0),
//                LocalDateTime.of(2023, 10, 1, 10, 0),
//                new ArrayList<>(), true
//        );
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(result.size(), 2);
//    }

    @Test
    void createEndpoint_Test() {
        when(repository.save(any(EndpointHit.class))).thenReturn(new EndpointHit());
        service.add(EndpointHitDto.builder().build());

        verify(repository, times(1)).save(any(EndpointHit.class));
        verifyNoMoreInteractions(repository);
    }

    private List<ViewEndpointDto> getListViewEndpointDto() {
        List<ViewEndpointDto> result = new ArrayList<>();

        result.add(ViewEndpointDto.builder()
                        .app("app2")
                        .uri("uri2")
                        .hits(200L)
                        .build()
        );

        result.add(ViewEndpointDto.builder()
                        .app("app2")
                        .uri("uri2")
                        .hits(200L)
                        .build()
        );

        return result;
    }
}