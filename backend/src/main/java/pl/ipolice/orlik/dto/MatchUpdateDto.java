package pl.ipolice.orlik.dto;

import pl.ipolice.orlik.model.enums.MatchType;

import java.time.LocalDateTime;
import java.util.List;

public record MatchUpdateDto(
        LocalDateTime matchDate,
        MatchType matchType,
        Long pitchId,
        Integer teamAScore,
        Integer teamBScore,
        List<MatchParticipationDto> participations
) {
}
