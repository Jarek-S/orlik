package pl.ipolice.orlik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ipolice.orlik.model.DemoPlayer;

@Repository
public interface DemoPlayerRepository extends JpaRepository<DemoPlayer, Long> {
}
