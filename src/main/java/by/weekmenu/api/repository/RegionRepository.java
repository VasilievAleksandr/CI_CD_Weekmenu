package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends CrudRepository<Region, Long> {

    Optional<Region> findByNameIgnoreCase(String name);
    List<Region> findAllByCountry_Id(Long id);
}
