package by.weekmenu.api.repository;

import by.weekmenu.api.entity.CookingMethod;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CookingMethodRepository extends CrudRepository<CookingMethod, Integer> {

    Optional<CookingMethod> findByNameIgnoreCase(String name);
    List<CookingMethod> findAllByIsArchivedIsFalse();

    @Modifying
    @Query("update CookingMethod e set e.isArchived = true where e.id = :cookingMethodId")
    void softDelete(@Param("cookingMethodId") Integer cookingMethodId);

    @Modifying
    @Query("update CookingMethod cookingMethod set cookingMethod.isArchived = false " +
            "where cookingMethod.id = :cookingMethodId")
    void restore(@Param("cookingMethodId") Integer cookingMethodId);
}
