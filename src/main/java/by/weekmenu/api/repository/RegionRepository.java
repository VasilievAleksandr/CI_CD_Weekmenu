package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Region;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends CrudRepository<Region, Long> {

    Optional<Region> findByNameIgnoreCase(String name);
    List<Region> findAllByCountry_Id(Long id);
    List<Region> findAllByIsArchivedIsFalse();

    @Modifying
    @Query("update Region region set region.isArchived = true where region.id = :regionId")
    void softDelete(@Param("regionId") Long regionId);

    @Modifying
    @Query("update Region region set region.isArchived = false where region.id = :regionId")
    void restore(@Param("regionId") Long regionId);
}
