package fr.parisnanterre.greentrip.backend.service;

import fr.parisnanterre.greentrip.backend.entity.Trip;
import fr.parisnanterre.greentrip.backend.entity.User;
import fr.parisnanterre.greentrip.backend.entity.Waypoint;
import fr.parisnanterre.greentrip.backend.repository.TripRepository;
import fr.parisnanterre.greentrip.backend.repository.UserRepository;
import fr.parisnanterre.greentrip.backend.repository.WayPointRepository;
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

    @Autowired
    private WayPointRepository waypointRepository;

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

        // Calculer la position de chaque waypoint
        int position = 1;
        for (Waypoint waypoint : waypoints) {
            waypoint.setPosition(position++);
            trip.addWaypoint(waypoint);
        }

        // Sauvegarder dans la base de données
        return tripRepository.save(trip);
    }


    @Transactional
    public void deleteTrip(Long userId, Long tripId) {
        // Vérifier que le trip appartient bien à l'utilisateur
        Trip trip = findUserTrip(userId, tripId);

        // Supprimer tous les waypoints associés
        waypointRepository.deleteAll(trip.getWaypoints());

        // Supprimer le trip
        tripRepository.delete(trip);
    }


    public Trip findUserTrip(Long userId, Long tripId) {
        return tripRepository.findById(tripId)
                .filter(trip -> trip.getUser().getId().equals(userId))  // Vérification avec getId() au lieu de comparer l'objet
                .orElseThrow(() -> new RuntimeException("Trip non trouvé ou n'appartient pas au user"));
    }

}
