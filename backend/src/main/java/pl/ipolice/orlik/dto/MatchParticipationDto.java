package pl.ipolice.orlik.dto;

import jakarta.validation.constraints.NotNull;
import pl.ipolice.orlik.model.enums.Team;

public record MatchParticipationDto(
        @NotNull Long playerId,
        @NotNull Team team,
        Integer goals,
        Integer assists,
        Boolean playedAsGoalkeeper
) {
}
