package fr.parisnanterre.greentrip.backend.repository;

import fr.parisnanterre.greentrip.backend.entity.Waypoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WayPointRepository extends JpaRepository<Waypoint, Long> {
}
