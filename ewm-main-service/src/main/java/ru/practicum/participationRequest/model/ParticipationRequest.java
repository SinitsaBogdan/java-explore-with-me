package ru.practicum.participationRequest.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.model.Event;
import ru.practicum.participationRequest.util.ParticipationRequestState;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ParticipationRequestState status;

    @Column(name = "created_date")
    private LocalDateTime created;
}