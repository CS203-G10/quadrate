package cs203t10.quadrate.location;

import cs203t10.quadrate.exception.LocationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocation(Long id) throws LocationNotFoundException {
        return locationRepository.findById(id).orElseThrow(() -> new LocationNotFoundException(id));
    }

    @Transactional
    public Location updateLocation(Long id, Location location) throws LocationNotFoundException {
        if (!locationRepository.existsById(id)) {
            throw new LocationNotFoundException(id);
        }

        locationRepository.updateLocation(id, location.getName(), location.getCapacity(), location.getBookable(), location.getParentLocation().getId());
        return getLocation(id);
    }

    @Transactional
    public Location removeLocation(Long id) throws LocationNotFoundException {
        Location location = getLocation(id);
        locationRepository.deleteById(id);
        return location;
    }
}
