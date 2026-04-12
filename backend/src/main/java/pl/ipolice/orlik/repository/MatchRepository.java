package pl.ipolice.orlik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ipolice.orlik.model.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

}
