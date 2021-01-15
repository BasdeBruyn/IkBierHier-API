package nl.ikbierhier.services;

import nl.ikbierhier.models.Location;
import nl.ikbierhier.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final GroupService groupService;

    @PersistenceContext
    EntityManager entityManager;

    public LocationService(LocationRepository locationRepository, GroupService groupService) {
        this.locationRepository = locationRepository;
        this.groupService = groupService;
    }

    public List<Location> getAllValidLocationsForUser() {
        return locationRepository.findAllByGroupInAndExpiresAtAfter(
                groupService.getAllGroupsForUser(),
                new Timestamp(System.currentTimeMillis())
        );
    }

    public Optional<Location> getLocation(UUID uuid) {
        return locationRepository.findById(uuid);
    }

    public Location shareLocation(Location location) {
        locationRepository.deleteByUserAndGroup(location.getUser(), location.getGroup());

        Location savedLocation = locationRepository.saveAndFlush(location);
        entityManager.refresh(savedLocation);

        return savedLocation;
    }


    public Location deleteLocation(Location location) {
        locationRepository.delete(location);
        return location;
    }
}
