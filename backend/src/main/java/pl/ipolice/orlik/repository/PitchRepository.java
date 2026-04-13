package pl.ipolice.orlik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ipolice.orlik.model.Pitch;

@Repository
public interface PitchRepository extends JpaRepository<Pitch, Long> {
}
