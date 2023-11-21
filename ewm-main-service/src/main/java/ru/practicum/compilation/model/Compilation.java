package ru.practicum.compilation.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "is_pinned")
    private Boolean pinned;

    @ManyToMany
    @JoinTable(
            name = "compilations_events",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events;
}