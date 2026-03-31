package pl.ipolice.orlik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ipolice.orlik.model.Group;
import pl.ipolice.orlik.model.Player;
import pl.ipolice.orlik.model.User;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    boolean existsByUserAndGroup(User resultUser, Group group);

    List<Player> findByGroupId(Long groupId);
}
