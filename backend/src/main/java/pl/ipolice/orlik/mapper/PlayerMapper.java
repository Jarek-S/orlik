package pl.ipolice.orlik.mapper;

import org.springframework.stereotype.Component;
import pl.ipolice.orlik.dto.PlayerDto;
import pl.ipolice.orlik.model.Player;

@Component
public class PlayerMapper {

    public PlayerDto toDto(Player player) {
        if (player == null) {
            return null;
        }

        return new PlayerDto(
                player.getId(),                  // Long id
                player.getFirstName(),           // String firstName
                player.getLastName(),            // String lastName
                player.getNickName(),            // String nickName
                player.getBirthYear(),           // Integer birthYear
                null,                            // will be calculated

                player.getSpeedRank(),           // Integer speedRank
                player.getTechniqueRank(),       // Integer techniqueRank
                player.getDefenseSkillRank(),    // Integer defenseSkillRank
                player.getDefenseWorkRate(),     // Integer defenseWorkRate
                player.getStamina(),             // Integer stamina

                // String primaryPosition from enum
                player.getPrimaryPosition() != null ? player.getPrimaryPosition().name() : null,

                player.getCanPlayAsGk(),          // boolean canPlayAsGk
                player.getIsGoalkeeperToday(),      // boolean isGoalkeeperToday
                player.getIsAdmin(),                // boolean isAdmin
                player.getIsCoach(),                // boolean isCoach

                // Relations
                player.getUser() != null ? player.getUser().getId() : null, // Long userId
                player.getGroup().getId(),                                  // Long groupId
                player.getJoinedAt()                                        // LocalDate joinedAt
        );
    }
}