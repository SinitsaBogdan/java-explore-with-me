package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
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
    private LocalDateTime timestamp;
}
