package pl.ipolice.orlik.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.ipolice.orlik.dto.MatchDto;
import pl.ipolice.orlik.dto.MatchParticipationDto;
import pl.ipolice.orlik.model.Match;
import pl.ipolice.orlik.model.MatchParticipation;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MatchMapper {
    public MatchDto toDto(Match match) {
        if (Objects.isNull(match)) {
            return null;
        }

        List<MatchParticipationDto> participationDtos = match.getParticipations()
                .stream().map(this::toParticipationDto)
                .toList();

        return new MatchDto(
                match.getId(),
                match.getMatchDate(),
                match.getMatchType(),
                match.getLocation(),
                match.getTeamAScore(),
                match.getTeamBScore(),
                participationDtos
        );
    }

    private MatchParticipationDto toParticipationDto(MatchParticipation participation) {
        return new MatchParticipationDto(
                Objects.nonNull(participation.getPlayer()) ? participation.getPlayer().getId() : null,
                participation.getTeam(),
                participation.getGoals(),
                participation.getAssists(),
                participation.isPlayedAsGoalkeeper()
        );
    }
}
