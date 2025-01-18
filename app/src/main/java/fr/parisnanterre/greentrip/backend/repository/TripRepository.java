package fr.parisnanterre.greentrip.backend.repository;

import fr.parisnanterre.greentrip.backend.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    /**
     * Récupère tous les trips créés par un utilisateur spécifique.
     *
     * @param userId ID de l'utilisateur
     * @return Liste des trips appartenant à l'utilisateur
     */
    List<Trip> findByUserId(Long userId);
}
