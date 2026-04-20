package pl.ipolice.orlik.dto;

import jakarta.validation.constraints.NotNull;
import pl.ipolice.orlik.model.enums.MatchType;

import java.time.LocalDateTime;

public record MatchSaveDto(
        @NotNull LocalDateTime matchDate,
        @NotNull MatchType matchType,
        @NotNull Long pitchId
) {
}
