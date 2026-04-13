package pl.ipolice.orlik.dto;

import jakarta.validation.constraints.NotNull;
import pl.ipolice.orlik.model.enums.MatchType;

import java.time.LocalDateTime;
import java.util.List;

public record MatchSaveDto(
        Long id,
        @NotNull LocalDateTime matchDate,
        @NotNull MatchType matchType,
        @NotNull Long pitchId,
        Integer teamAScore,
        Integer teamBScore,

        List<MatchParticipationDto> participations
) {
}
