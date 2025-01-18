package fr.parisnanterre.greentrip.backend.service;

import fr.parisnanterre.greentrip.backend.entity.Trip;
import fr.parisnanterre.greentrip.backend.entity.User;
import fr.parisnanterre.greentrip.backend.entity.Waypoint;
import fr.parisnanterre.greentrip.backend.repository.TripRepository;
import fr.parisnanterre.greentrip.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Trip> getTripsByUser(Long userId) {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId));

        // Récupérer tous les trips de l'utilisateur
        return tripRepository.findByUserId(userId);
    }

    @Transactional
    public Trip saveTrip(Long userId, String tripName, List<Waypoint> waypoints) {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId));

        // Créer le trajet
        Trip trip = new Trip();
        trip.setName(tripName);
        trip.setUser(user);

        // Associer les waypoints au trajet
        waypoints.forEach(waypoint -> trip.addWaypoint(waypoint));

        // Sauvegarder dans la base de données
        return tripRepository.save(trip);
    }
}
