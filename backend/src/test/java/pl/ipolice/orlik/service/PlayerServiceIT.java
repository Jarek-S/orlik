package pl.ipolice.orlik.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ipolice.orlik.BaseIntegrationTest;
import pl.ipolice.orlik.dto.PlayerDto;
import pl.ipolice.orlik.dto.PlayerSaveDto;
import pl.ipolice.orlik.model.Group;
import pl.ipolice.orlik.model.Player;
import pl.ipolice.orlik.model.User;
import pl.ipolice.orlik.model.enums.PrimaryPosition;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.PlayerRepository;
import pl.ipolice.orlik.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("database")
@Tag("service")
class PlayerServiceIT extends BaseIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlayerService playerService;

    private Group savedGroup;

    @BeforeEach
    void setUp() {
        User groupOwner = new User(); // group owner must exist
        groupOwner.setKeycloakId("owner-id");
        groupOwner.setEmail("owner@test.pl");
        groupOwner.setCreatedAt(LocalDate.now());
        User savedUser = userRepository.save(groupOwner);

        Group group = new Group();
        group.setName("Camp_Nou");
        group.setOwner(savedUser);
        group.setCreatedAt(LocalDate.now());
        savedGroup = groupRepository.save(group);

    }

    @Test
    void shouldCreatePlayerSuccessfully() {
        // Given
        PlayerSaveDto createDto = new PlayerSaveDto(
                "Adam", "Young", "nickAdam", 2000,
                PrimaryPosition.ATT.name()
        );

        // When
        PlayerDto result = playerService.createPlayer(savedGroup.getId(), createDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.firstName()).isEqualTo("Adam");
        assertThat(result.age()).isEqualTo(LocalDate.now().getYear() - 2000);
        assertThat(result.groupId()).isEqualTo(savedGroup.getId());

        Optional<Player> dbPlayer = playerRepository.findById(result.id());
        assertThat(dbPlayer.isPresent());
        dbPlayer.ifPresent(player -> assertThat(player.getGroup().getId()).isEqualTo(savedGroup.getId()));
        dbPlayer.ifPresent(player -> assertThat(player.getNickName()).isEqualTo(createDto.nickName()));
    }

    @Test
    void shouldFailWhenCreatingPlayerForNonExistentGroup() {
        // Given
        PlayerSaveDto createDto = new PlayerSaveDto(
                "Adam", "Young", "nickAdam", 2000,
                PrimaryPosition.ATT.name()
        );

        // When & Then
        assertThatThrownBy(() -> playerService.createPlayer(999L, createDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdatePlayerAndReflectChangesInDb() {
        // Given
        Player player = new Player();
        player.setFirstName("Old");
        player.setLastName("Player");
        player.setNickName("OldNick");
        player.setGroup(savedGroup);
        player = playerRepository.save(player);

        PlayerDto updateData = new PlayerDto(
                player.getId(), "Mark", "Changed", "newNick", 1985,
                10, 10, 10, 10, 10,
                11, PrimaryPosition.GK.name(), false, true, true, true,
                null, savedGroup.getId(), LocalDate.now().minusDays(2)
        );

        // When
        playerService.updatePlayer(updateData);

        // Then
        Player result = playerRepository.findById(player.getId()).orElseThrow();
        assertThat(result.getFirstName()).isEqualTo("Mark");
        assertThat(result.getPrimaryPosition()).isEqualTo(PrimaryPosition.GK);
        assertThat(result.getIsAdmin()).isTrue();
        assertThat(result.getBirthYear()).isEqualTo(1985);
        assertThat(result.getJoinedAt()).isEqualTo(player.getJoinedAt()); // this date should remain unchanged
    }
}
