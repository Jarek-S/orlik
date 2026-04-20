package pl.ipolice.orlik.service;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ipolice.orlik.BaseIntegrationTest;
import pl.ipolice.orlik.dto.MatchDto;
import pl.ipolice.orlik.dto.MatchSaveDto;
import pl.ipolice.orlik.model.Match;
import pl.ipolice.orlik.model.Player;
import pl.ipolice.orlik.model.enums.MatchType;
import pl.ipolice.orlik.repository.MatchRepository;
import pl.ipolice.orlik.repository.PlayerRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("database")
@Tag("service")
class MatchServiceIT extends BaseIntegrationTest {

    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayerRepository playerRepository;

    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        baseSetup();

        player1 = new Player();
        player1.setFirstName("Adam");
        player1.setNickName("Adi");
        player1.setGroup(defaultGroup);
        player1 = playerRepository.save(player1);


        player2 = new Player();
        player2.setFirstName("Bart");
        player2.setGroup(defaultGroup);
        player2 = playerRepository.save(player2);
    }

    @Test
    void shouldCreateMatchWithParticipations() {
        // Given
        MatchSaveDto matchDto = new MatchSaveDto(
                LocalDateTime.now().plusDays(1),
                MatchType.INTERNAL,
                null
        );

        // When
        MatchDto result = matchService.createMatch(defaultGroup.getId(), matchDto);

        // Then
        assertThat(result.id()).isNotNull();

        Match dbMatch = matchRepository.findById(result.id()).orElseThrow(EntityNotFoundException::new);
        assertThat(dbMatch.getParticipations().size()).isEqualTo(0);
    }

    @Test
    void shouldThrowExceptionWhenDateNotFromFuture() {
        // Given
        MatchSaveDto dto = new MatchSaveDto(LocalDateTime.now(), MatchType.INTERNAL, null);

        // When & Then
        assertThatThrownBy(() -> matchService.createMatch(defaultGroup.getId(), dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("future");
    }
}
