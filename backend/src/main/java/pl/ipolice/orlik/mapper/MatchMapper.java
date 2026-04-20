package pl.ipolice.orlik.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.ipolice.orlik.dto.MatchDto;
import pl.ipolice.orlik.dto.MatchParticipationDto;
import pl.ipolice.orlik.dto.PitchDto;
import pl.ipolice.orlik.model.Match;
import pl.ipolice.orlik.model.MatchParticipation;
import pl.ipolice.orlik.model.Pitch;

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
                Objects.nonNull(match.getPitch()) ? toPitchDto(match.getPitch()) : null,
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
                participation.isPlayedAsGoalkeeper(),
                participation.isMvp()
        );
    }

    private PitchDto toPitchDto(Pitch pitch) {
        return new PitchDto(pitch.getId(), pitch.getName(), pitch.getCity());
    }
}
