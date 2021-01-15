package nl.ikbierhier.controllers;

import nl.ikbierhier.models.Location;
import nl.ikbierhier.services.LocationService;
import nl.ikbierhier.statusErrors.NotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public Iterable<Location> getAllLocations() {
        return locationService.getAllValidLocationsForUser();
    }

    @GetMapping("/{uuid}")
    public Location getLocation(@PathVariable UUID uuid) {
        return locationService
                .getLocation(uuid)
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping("/share")
    @Transactional
    public Location shareLocation(@Valid @RequestBody Location location) {
        return locationService.shareLocation(location);
    }

    @DeleteMapping("/delete")
    public Location deleteLocation(@Valid @RequestBody Location location) {
        locationService.deleteLocation(location);
        return location;

    }
}
