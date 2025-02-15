package fr.parisnanterre.greentrip.backend.service;

import fr.parisnanterre.greentrip.backend.entity.Trip;
import fr.parisnanterre.greentrip.backend.entity.Waypoint;
import fr.parisnanterre.greentrip.backend.repository.TripRepository;
import fr.parisnanterre.greentrip.backend.repository.WayPointRepository;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaypointService {

    private final TripService tripService;

    @Autowired
    private WayPointRepository wayPointRepository;

    @Autowired
    private TripRepository tripRepository;

    public WaypointService(TripService tripService) {
        this.tripService = tripService;
    }

    @Transactional
    public Trip addWaypointToTrip(Long userId, Long tripId, Waypoint waypoint) {
        Trip trip = tripService.findUserTrip(userId, tripId);

        waypoint.setTrip(trip);

        // Trouver la position maximale actuelle et ajouter 1
        int maxPosition = trip.getWaypoints().stream()
                .mapToInt(Waypoint::getPosition)
                .max()
                .orElse(0); // Si aucun waypoint n'existe, commence à 1
        waypoint.setPosition(maxPosition + 1);

        trip.addWaypoint(waypoint);

        wayPointRepository.save(waypoint);
        return tripRepository.save(trip);
    }

    @Transactional
    public Waypoint updateWaypoint(Long userId, Long tripId, Long waypointId, Double lat, Double lon) {
        Trip trip = tripService.findUserTrip(userId,tripId);

        Waypoint waypoint = trip.getWaypoints().stream()
                .filter(wp -> wp.getId().equals(waypointId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Point non trouvé avec l'ID : " + waypointId));

        waypoint.setLatitude(lat);
        waypoint.setLongitude(lon);

        return wayPointRepository.save(waypoint);
    }

    @Transactional
    public Trip deleteWaypoint(Long userId, Long tripId, Long waypointId) {
        Trip trip = tripService.findUserTrip(userId,tripId);

        Waypoint waypointToRemove = trip.getWaypoints().stream()
                                .filter(wp -> wp.getId().equals(waypointId))
                                .findFirst()
                .orElseThrow(() -> new RuntimeException("Waypoint non trouvé"));

        int removedPosition = waypointToRemove.getPosition();

        trip.getWaypoints().remove(waypointToRemove);
        wayPointRepository.deleteById(waypointId);

        trip.getWaypoints().stream()
                .filter(wp -> wp.getPosition() > removedPosition)
                .forEach(wp -> wp.setPosition(wp.getPosition() - 1));

        return tripRepository.save(trip);
    }
}
