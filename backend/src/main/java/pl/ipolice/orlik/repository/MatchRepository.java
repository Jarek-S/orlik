package pl.ipolice.orlik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ipolice.orlik.dto.MatchDto;
import pl.ipolice.orlik.model.Match;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<MatchDto> getMatchById(Long id);
}
