package pl.ipolice.orlik.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ipolice.orlik.dto.MatchDto;
import pl.ipolice.orlik.dto.MatchParticipationDto;
import pl.ipolice.orlik.dto.MatchSaveDto;
import pl.ipolice.orlik.mapper.MatchMapper;
import pl.ipolice.orlik.model.*;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.MatchRepository;
import pl.ipolice.orlik.repository.PitchRepository;
import pl.ipolice.orlik.repository.PlayerRepository;

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

    public MatchDto createMatch(Long groupId, MatchSaveDto dto) {
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
        match.setTeamAScore(dto.teamAScore());
        match.setTeamBScore(dto.teamBScore());

        if (dto.participations() != null) {
            for (MatchParticipationDto partDto : dto.participations()) {

                Player player = playerRepository.findById(partDto.playerId())
                        .orElseThrow(() -> new EntityNotFoundException("No player with id: " + partDto.playerId()));

                MatchParticipation participation = getMatchParticipation(partDto, player);

                match.addParticipation(participation);
            }
        }

        Match savedMatch = matchRepository.save(match);

        return matchMapper.toDto(savedMatch);
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
}
