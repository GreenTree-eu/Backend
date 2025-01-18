package fr.parisnanterre.greentrip.backend.dto;

import fr.parisnanterre.greentrip.backend.entity.Waypoint;
import java.util.List;

public class TripRequest {
    private String name;
    private List<Waypoint> waypoints;

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }
}
