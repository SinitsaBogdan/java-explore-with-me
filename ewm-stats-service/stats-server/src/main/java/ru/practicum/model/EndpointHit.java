package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "endpoints", schema = "public")
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app")
    @NotBlank(message = "Параметр app не может быть пустым")
    private String app;

    @Column(name = "uri")
    @NotBlank(message = "Параметр uri не может быть пустым")
    private String uri;

    @Column(name = "ip")
    @NotBlank(message = "Параметр ip не может быть пустым")
    private String ip;

    @Column(name = "time_stamp")
    @NotNull(message = "Параметр time_stamp не может быть пустым")
    private LocalDateTime hitTimestamp;
}
