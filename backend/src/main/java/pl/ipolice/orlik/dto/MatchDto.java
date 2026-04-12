package pl.ipolice.orlik.dto;

import jakarta.validation.constraints.NotNull;
import pl.ipolice.orlik.model.enums.MatchType;

import java.time.LocalDateTime;
import java.util.List;

public record MatchDto(
        Long id,
        @NotNull LocalDateTime matchDate,
        @NotNull MatchType matchType,
        @NotNull String location,
        Integer teamAScore,
        Integer teamBScore,

        List<MatchParticipationDto> participations
) {
}
