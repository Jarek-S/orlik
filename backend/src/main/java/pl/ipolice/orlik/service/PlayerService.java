package pl.ipolice.orlik.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ipolice.orlik.dto.PlayerDto;
import pl.ipolice.orlik.dto.PlayerSaveDto;
import pl.ipolice.orlik.mapper.PlayerMapper;
import pl.ipolice.orlik.model.Group;
import pl.ipolice.orlik.model.Player;
import pl.ipolice.orlik.model.enums.PrimaryPosition;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.PlayerRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final GroupRepository groupRepository;
    private final PlayerMapper playerMapper;

    public PlayerDto createPlayer(Long groupId, PlayerSaveDto dto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        Player player = new Player();

        updateEntityFromCreateDto(player, dto);

        player.setGroup(group);
        player.setJoinedAt(LocalDate.now());

        Player savedPlayer = playerRepository.save(player);
        return playerMapper.toDto(savedPlayer);
    }

    @Transactional(readOnly = true)
    public PlayerDto getPlayer(Long playerId) {
        return playerRepository.findById(playerId)
                .map(playerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));
    }

    @Transactional(readOnly = true)
    public List<PlayerDto> getPlayersByGroup(Long groupId) {
        return playerRepository.findByGroupId(groupId).stream()
                .map(playerMapper::toDto)
                .toList();
    }

    public PlayerDto updatePlayer(PlayerDto dto) {
        Player player = playerRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));

        updateEntityFromDto(player, dto);

        Player updatedPlayer = playerRepository.save(player);
        return playerMapper.toDto(updatedPlayer);
    }

    public void deletePlayer(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new EntityNotFoundException("Player not found");
        }
        playerRepository.deleteById(playerId);
    }

    private void updateEntityFromDto(Player player, PlayerDto dto) {
        player.setFirstName(dto.firstName());
        player.setLastName(dto.lastName());
        player.setNickName(dto.nickName());
        player.setBirthYear(dto.birthYear());

        player.setSpeedRank(dto.speedRank());
        player.setTechniqueRank(dto.techniqueRank());
        player.setDefenseSkillRank(dto.defenseSkillRank());
        player.setDefenseWorkRate(dto.defenseWorkRate());
        player.setStamina(dto.stamina());

        if (dto.primaryPosition() != null) {
            player.setPrimaryPosition(PrimaryPosition.valueOf(dto.primaryPosition().toUpperCase()));
        }

        player.setCanPlayAsGk(dto.canPlayAsGk());
        player.setIsGoalkeeperToday(dto.isGoalkeeperToday());
        player.setIsAdmin(dto.isAdmin());
        player.setIsCoach(dto.isCoach());
    }

    private void updateEntityFromCreateDto(Player player, PlayerSaveDto dto) {
        player.setFirstName(dto.firstName());
        player.setLastName(dto.lastName());
        player.setNickName(dto.nickName());
        player.setBirthYear(dto.birthYear());

        if (dto.primaryPosition() != null) {
            player.setPrimaryPosition(PrimaryPosition.valueOf(dto.primaryPosition().toUpperCase()));
        }

    }
}
