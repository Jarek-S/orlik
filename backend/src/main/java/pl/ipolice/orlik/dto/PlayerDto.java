package pl.ipolice.orlik.dto;

import java.time.LocalDate;

public record PlayerDto(
        Long id,
        String firstName,
        String lastName,
        String nickName,
        Integer birthYear,
        Integer age, // calculated

        Integer speedRank,
        Integer techniqueRank,
        Integer defenseSkillRank,
        Integer defenseWorkRate,
        Integer stamina,

        String primaryPosition,
        Boolean canPlayAsGk,
        Boolean isGoalkeeperToday,
        Boolean isAdmin,
        Boolean isCoach,

        Long userId,
        Long groupId,

        LocalDate joinedAt
) {
    public PlayerDto {
        if (birthYear != null) {
            age = LocalDate.now().getYear() - birthYear;
        }
    }
}

