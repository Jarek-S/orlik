package pl.ipolice.orlik.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ipolice.orlik.dto.MatchDto;
import pl.ipolice.orlik.dto.MatchParticipationDto;
import pl.ipolice.orlik.dto.MatchSaveDto;
import pl.ipolice.orlik.dto.MatchUpdateDto;
import pl.ipolice.orlik.mapper.MatchMapper;
import pl.ipolice.orlik.model.*;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.MatchRepository;
import pl.ipolice.orlik.repository.PitchRepository;
import pl.ipolice.orlik.repository.PlayerRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final GroupRepository groupRepository;
    private final PlayerRepository playerRepository;
    private final MatchMapper matchMapper;
    private final PitchRepository pitchRepository;

    public MatchDto getMatchById(@Argument Long id) {
        return matchRepository.getMatchById(id).orElse(null);
    }

    public MatchDto createMatch(Long groupId, MatchSaveDto dto) {

        validateMatchCreation(dto);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("No group with id: " + groupId));

        Match match = new Match();
        match.setGroup(group);
        match.setMatchDate(dto.matchDate());
        match.setMatchType(dto.matchType());
        if (Objects.nonNull(dto.pitchId())) {
            Pitch pitch = pitchRepository.findById(dto.pitchId())
                    .orElseThrow(() -> new EntityNotFoundException("No pitch with id: " + dto.pitchId()));
            match.setPitch(pitch);
        }

        Match savedMatch = matchRepository.save(match);

        return matchMapper.toDto(savedMatch);
    }

    public MatchDto updateMatch(Long matchId, MatchUpdateDto dto) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("No match with id: " + matchId));

        if (dto.matchDate() != null) {
            match.setMatchDate(dto.matchDate());
        }
        if (dto.matchType() != null) {
            match.setMatchType(dto.matchType());
        }
        if (dto.teamAScore() != null) {
            match.setTeamAScore(dto.teamAScore());
        }
        if (dto.teamBScore() != null) {
            match.setTeamBScore(dto.teamBScore());
        }

        if (dto.pitchId() != null) {
            Pitch pitch = pitchRepository.findById(dto.pitchId())
                    .orElseThrow(() -> new EntityNotFoundException("No pitch with id: " + dto.pitchId()));
            match.setPitch(pitch);
        }

        if (dto.participations() != null) {
            validateGoals(match, dto);

            match.getParticipations().clear();

            for (MatchParticipationDto partDto : dto.participations()) {
                Player player = playerRepository.findById(partDto.playerId())
                        .orElseThrow(() -> new EntityNotFoundException("No player with id: " + partDto.playerId()));

                MatchParticipation participation = getMatchParticipation(partDto, player);
                match.addParticipation(participation);
            }
        }

        Match updatedMatch = matchRepository.save(match);

        return matchMapper.toDto(updatedMatch);
    }

    private static @NonNull MatchParticipation getMatchParticipation(MatchParticipationDto partDto, Player player) {
        MatchParticipation participation = new MatchParticipation();
        participation.setPlayer(player);
        participation.setTeam(partDto.team());

        participation.setGoals(partDto.goals() != null ? partDto.goals() : 0);
        participation.setAssists(partDto.assists() != null ? partDto.assists() : 0);
        participation.setPlayedAsGoalkeeper(Boolean.TRUE.equals(partDto.playedAsGoalkeeper()));
        return participation;
    }

    private static void validateMatchCreation(MatchSaveDto match) {
        if (match.matchDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Match date must be in the future!");
        }
    }

    private void validateGoals(Match match, MatchUpdateDto dto) {
        Integer scoreA = dto.teamAScore() != null ? dto.teamAScore() : match.getTeamAScore();
        Integer scoreB = dto.teamBScore() != null ? dto.teamBScore() : match.getTeamBScore();

        // if no result, no validation
        if (scoreA == null || scoreB == null) {
            return;
        }

        int sumGoals = dto.participations().stream()
                .mapToInt(p -> p.goals() != null ? p.goals() : 0)
                .sum();

        // TODO: real validation for match types
        if (sumGoals != (scoreA + scoreB)) {
            throw new IllegalArgumentException(
                    "Validation error: match score and goals sum has to be equal."
            );
        }
    }
}
