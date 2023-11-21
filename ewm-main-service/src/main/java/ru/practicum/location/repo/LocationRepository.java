package ru.practicum.location.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.location.model.Location;

public interface LocationRepository extends JpaRepository<Location, Float> {

    Location findByLatAndLon(float lat, float lon);
}
