package pl.ipolice.orlik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ipolice.orlik.model.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByOwnerId(Long ownerId);
}
