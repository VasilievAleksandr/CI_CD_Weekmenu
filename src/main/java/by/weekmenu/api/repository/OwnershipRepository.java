package by.weekmenu.api.repository;

import by.weekmenu.api.entity.Ownership;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OwnershipRepository extends CrudRepository<Ownership, Long> {

    Optional<Ownership> findByName(String name);
}
