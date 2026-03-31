package pl.ipolice.orlik.dto;

import java.time.LocalDate;

public record PlayerCreateDto(
        String firstName,
        String lastName,
        String nickName,
        Integer birthYear,
        String primaryPosition
) {
    public PlayerCreateDto {
        if (birthYear != null && (birthYear > LocalDate.now().getYear() || birthYear < LocalDate.now().getYear() - 100)) {
            throw new IllegalArgumentException("Birth year out of range");
        }
    }
}
