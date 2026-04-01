package pl.ipolice.orlik.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import pl.ipolice.orlik.BaseIntegrationTest;
import pl.ipolice.orlik.model.Group;
import pl.ipolice.orlik.model.Invitation;
import pl.ipolice.orlik.model.User;
import pl.ipolice.orlik.model.enums.InvitationStatus;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.InvitationRepository;
import pl.ipolice.orlik.repository.PlayerRepository;
import pl.ipolice.orlik.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("database")
@Tag("service")
public class UserServiceIT extends BaseIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Nested
    @DisplayName("New user registration")
    class NewUserRegistration {
        @Test
        @DisplayName("should create user and his own group when user not exist and has no invite code")
        void shouldCreateUserAndHisOwnGroup() {
            //given
            String keycloakId = "abc-123";
            String email = "user@test.pl";
            Jwt jwt = createMockJwt(keycloakId, email);
            String inviteCode = "";

            //when
            User newUser = userService.synchronizeUser(jwt, inviteCode);

            //then
            assertThat(newUser.getId()).isNotNull();
            assertThat(newUser.getEmail()).isEqualTo("user@test.pl");
            assertThat(newUser.getKeycloakId()).isEqualToIgnoringCase("abc-123");
            List<Group> userGroups = groupRepository.findAllByOwnerId(newUser.getId());
            assertThat(userGroups.size()).isEqualTo(1);
            Group userGroup = userGroups.getFirst();
            assertThat(userGroup.getCreatedAt()).isEqualTo(newUser.getCreatedAt());
            assertThat(userGroup.getOwner().getId()).isEqualTo(newUser.getId());
            assertThat(userGroup.getName()).isNull();
        }

        @Test
        @DisplayName("should not create user and his own group when exist and has no invite code")
        void shouldNotCreateUserAndHisOwnGroup() {
            //given
            String keycloakId = "abc-123";
            String email = "user@test.pl";
            Jwt jwt = createMockJwt(keycloakId, email);
            String inviteCode = "";
            User createdUser = userService.synchronizeUser(jwt, inviteCode);
            Group createdUserGroup = groupRepository.findAllByOwnerId(createdUser.getId()).getFirst();

            //when
            User existingUser = userService.synchronizeUser(jwt, inviteCode);

            //then
            assertThat(existingUser.getId()).isEqualTo(createdUser.getId());
            assertThat(existingUser.getKeycloakId()).isEqualToIgnoringCase("abc-123");
            List<Group> userGroups = groupRepository.findAllByOwnerId(existingUser.getId());
            assertThat(userGroups.size()).isEqualTo(1);
            Group userGroup = userGroups.getFirst();
            assertThat(userGroup.getCreatedAt()).isEqualTo(createdUserGroup.getCreatedAt());
            assertThat(userGroup.getId()).isEqualTo(createdUserGroup.getId());
        }
    }

    @Nested
    @DisplayName("Player invitation")
    class NewPlayerInvitation {
        @Test
        void shouldRegisterNewUserAndJoinGroup_whenInviteIsValid() {
            // Given
            User groupOwner = new User(); // group owner must exist
            groupOwner.setKeycloakId("owner-id");
            groupOwner.setEmail("owner@test.pl");
            groupOwner.setCreatedAt(LocalDate.now());
            userRepository.save(groupOwner);

            Group group = new Group();
            group.setName("Camp_Nou");
            group.setOwner(groupOwner);
            group.setCreatedAt(LocalDate.now());
            groupRepository.save(group);

            Invitation invitation = new Invitation();
            invitation.setGroup(group);
            invitation.setPlayerName("John");
            invitation.setStatus(InvitationStatus.PENDING);
            invitation.setExpiresAt(LocalDateTime.now().plusDays(1)); // valid until tomorrow
            invitationRepository.save(invitation);

            Jwt jwt = createMockJwt("new-player-id", "player@test.pl");
            String inviteCode = invitation.getCode().toString();

            // When
            User resultUser = userService.synchronizeUser(jwt, inviteCode);

            // Then
            assertThat(resultUser.getId()).isNotNull();
            assertThat(resultUser.getEmail()).isEqualTo("player@test.pl");
            assertThat(groupRepository.findAllByOwner(resultUser)).isEmpty();
            assertThat(playerRepository.existsByUserAndGroup(resultUser, group)).isTrue();
            Invitation updatedInvitation = invitationRepository.findByCode(invitation.getCode()).orElseThrow();
            assertThat(updatedInvitation.getStatus()).isEqualTo(InvitationStatus.ACCEPTED);
        }

        @Test
        void shouldAddExistingUserToGroup_whenInviteIsValid() {
            // Given
            User existingUser = new User();
            existingUser.setKeycloakId("existing-id");
            existingUser.setEmail("existing@test.pl");
            existingUser.setCreatedAt(LocalDate.now());
            userRepository.save(existingUser);

            User groupOwner = new User(); // group owner must exist
            groupOwner.setKeycloakId("owner-id");
            groupOwner.setEmail("owner@test.pl");
            groupOwner.setCreatedAt(LocalDate.now());
            userRepository.save(groupOwner);

            Group group = new Group();
            group.setName("Camp_Nou");
            group.setOwner(groupOwner);
            group.setCreatedAt(LocalDate.now());
            groupRepository.save(group);

            Invitation invitation = new Invitation();
            invitation.setGroup(group);
            invitation.setPlayerName("John");
            invitation.setStatus(InvitationStatus.PENDING);
            invitation.setExpiresAt(LocalDateTime.now().plusDays(1));
            invitationRepository.save(invitation);

            Jwt jwt = createMockJwt("existing-id", "existing@test.pl");

            // When
            User resultUser = userService.synchronizeUser(jwt, invitation.getCode().toString());

            // Then
            assertThat(resultUser.getId()).isEqualTo(existingUser.getId());
            assertThat(playerRepository.existsByUserAndGroup(resultUser, group)).isTrue();
            assertThat(userRepository.count()).isEqualTo(2);
        }

        @Test
        void shouldThrowException_whenInvitationIsExpired() {
            // Given
            User groupOwner = new User(); // group owner must exist
            groupOwner.setKeycloakId("owner-id");
            groupOwner.setEmail("owner@test.pl");
            groupOwner.setCreatedAt(LocalDate.now());
            userRepository.save(groupOwner);

            Group group = new Group();
            group.setName("Camp_Nou");
            group.setOwner(groupOwner);
            group.setCreatedAt(LocalDate.now());
            groupRepository.save(group);

            Invitation expiredInvitation = new Invitation();
            expiredInvitation.setGroup(group);
            expiredInvitation.setPlayerName("John");
            expiredInvitation.setStatus(InvitationStatus.PENDING);
            expiredInvitation.setExpiresAt(LocalDateTime.now().minusDays(1)); // expired yesterday
            invitationRepository.save(expiredInvitation);

            Jwt jwt = createMockJwt("some-id", "some@test.pl");

            // When & Then
            assertThatThrownBy(() -> userService.synchronizeUser(jwt, expiredInvitation.getCode().toString()))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("expired");
            assertThat(userRepository.count()).isEqualTo(1);
        }
    }


    private Jwt createMockJwt(String keycloakId, String email) {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(keycloakId);
        when(jwt.getClaimAsString("email")).thenReturn(email);
        return jwt;
    }
}
