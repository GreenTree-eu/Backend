package fr.parisnanterre.greentrip.backend.controller;

import fr.parisnanterre.greentrip.backend.dto.TripRequest;
import fr.parisnanterre.greentrip.backend.entity.Trip;
import fr.parisnanterre.greentrip.backend.entity.User;
import fr.parisnanterre.greentrip.backend.entity.Waypoint;
import fr.parisnanterre.greentrip.backend.service.TripService;
import fr.parisnanterre.greentrip.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;
    private final UserService userService;

    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getTripsByUser(@PathVariable Long userId) {
        List<Trip> trips = tripService.getTripsByUser(userId);
        if (trips.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si aucun trajet trouvé
        }
        return ResponseEntity.ok(trips);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Trip> saveTrip(
            @PathVariable Long userId,
            @RequestBody TripRequest tripRequest) {

        // Récupérer les données depuis TripRequest
        String tripName = tripRequest.getName();
        List<Waypoint> waypoints = tripRequest.getWaypoints();

        // Sauvegarder le trajet
        Trip trip = tripService.saveTrip(userId, tripName, waypoints);
        return ResponseEntity.ok(trip);
    }
}
