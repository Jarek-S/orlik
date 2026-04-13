package pl.ipolice.orlik.service;


import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ipolice.orlik.BaseIntegrationTest;
import pl.ipolice.orlik.dto.MatchDto;
import pl.ipolice.orlik.dto.MatchParticipationDto;
import pl.ipolice.orlik.dto.MatchSaveDto;
import pl.ipolice.orlik.model.Match;
import pl.ipolice.orlik.model.MatchParticipation;
import pl.ipolice.orlik.model.Player;
import pl.ipolice.orlik.model.enums.MatchType;
import pl.ipolice.orlik.model.enums.Team;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.MatchRepository;
import pl.ipolice.orlik.repository.PlayerRepository;

import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private GroupRepository groupRepository;

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
        MatchParticipationDto p1 = new MatchParticipationDto(
                player1.getId(), Team.TEAM_A, 2, 1, false
        );
        MatchParticipationDto p2 = new MatchParticipationDto(
                player2.getId(), Team.TEAM_B, 0, 0, true
        );


        MatchSaveDto matchDto = new MatchSaveDto(
                null,
                LocalDateTime.now(),
                MatchType.INTERNAL,
                null,
                2,
                1,
                List.of(p1, p2)
        );

        // When
        MatchDto result = matchService.createMatch(defaultGroup.getId(), matchDto);

        // Then
        assertThat(result.id()).isNotNull();
        assertThat(result.participations().size()).isEqualTo(2);

        Match dbMatch = matchRepository.findById(result.id()).orElseThrow(EntityNotFoundException::new);
        assertThat(dbMatch.getParticipations().size()).isEqualTo(2);
        assertThat(dbMatch.getTeamAScore()).isEqualTo(2);

        MatchParticipation p1Stats = dbMatch.getParticipations().stream()
                .filter(p -> p.getPlayer().getId().equals(player1.getId()))
                .findFirst().orElse(new MatchParticipation());

        assertThat(p1Stats.getGoals()).isEqualTo(2);
        assertThat(p1Stats.getAssists()).isEqualTo(1);
        assertThat(p1Stats.isPlayedAsGoalkeeper()).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenPlayerNotFound() {
        // Given
        MatchParticipationDto invalidPart = new MatchParticipationDto(
                999L, Team.TEAM_A, 0, 0, false
        );
        MatchSaveDto dto = new MatchSaveDto(null, LocalDateTime.now(), MatchType.INTERNAL, null, 0, 0, List.of(invalidPart));

        // When & Then
        assertThatThrownBy(() -> matchService.createMatch(defaultGroup.getId(), dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No player");
    }
}
