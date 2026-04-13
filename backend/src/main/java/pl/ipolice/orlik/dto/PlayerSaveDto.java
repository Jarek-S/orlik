package pl.ipolice.orlik.dto;

import java.time.LocalDate;

public record PlayerSaveDto(
        String firstName,
        String lastName,
        String nickName,
        Integer birthYear,
        String primaryPosition
) {
    public PlayerSaveDto {
        if (birthYear != null && (birthYear > LocalDate.now().getYear() || birthYear < LocalDate.now().getYear() - 100)) {
            throw new IllegalArgumentException("Birth year out of range");
        }
    }
}
