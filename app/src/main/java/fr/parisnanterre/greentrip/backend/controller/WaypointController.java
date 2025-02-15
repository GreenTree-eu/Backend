package fr.parisnanterre.greentrip.backend.controller;

import fr.parisnanterre.greentrip.backend.entity.Trip;
import fr.parisnanterre.greentrip.backend.entity.Waypoint;
import fr.parisnanterre.greentrip.backend.service.WaypointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waypoints")
public class WaypointController {

    private final WaypointService waypointService;

    @Autowired
    public WaypointController(WaypointService waypointService) {
        this.waypointService = waypointService;
    }

    @PostMapping("/{userId}/{tripId}/waypoints")
    public ResponseEntity<Trip> addWaypoint(@PathVariable Long userId, @PathVariable Long tripId, @RequestBody Waypoint waypoint) {
        return ResponseEntity.ok(waypointService.addWaypointToTrip(userId, tripId, waypoint));
    }


    @PutMapping("/{userId}/{tripId}/waypoints/{waypointId}")
    public ResponseEntity<Waypoint> updateWaypoint(@PathVariable Long userId, @PathVariable Long tripId, @PathVariable Long waypointId,
                                                   @RequestParam Double latitude, @RequestParam Double longitude) {
        return ResponseEntity.ok(waypointService.updateWaypoint(userId, tripId, waypointId, latitude, longitude));
    }

    @DeleteMapping("/{userId}/{tripId}/waypoints/{waypointId}")
    public ResponseEntity<Trip> deleteWaypoint(@PathVariable Long userId, @PathVariable Long tripId, @PathVariable Long waypointId) {
        return ResponseEntity.ok(waypointService.deleteWaypoint(userId, tripId, waypointId));
    }
}
