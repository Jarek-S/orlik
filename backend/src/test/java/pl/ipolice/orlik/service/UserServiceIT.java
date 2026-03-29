package pl.ipolice.orlik.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import pl.ipolice.orlik.BaseIntegrationTest;
import pl.ipolice.orlik.model.Group;
import pl.ipolice.orlik.model.User;
import pl.ipolice.orlik.repository.GroupRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("database")
public class UserServiceIT extends BaseIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private GroupRepository groupRepository;

    @Nested
    @DisplayName("New user registration")
    class NewUserRegistration {
        @Test
        @Transactional
        @DisplayName("should create user and his own group when user not exist and has no invite code")
        void shouldCreateUserAndHisOwnGroup() {
            //given
            Jwt jwt = Jwt.withTokenValue("mock-token")
                    .header("alg", "none")
                    .claim("sub", "abc-123")
                    .claim("email", "user@test.pl")
                    .build();
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
        @Transactional
        @DisplayName("should not create user and his own group when exist and has no invite code")
        void shouldNotCreateUserAndHisOwnGroup() {
            //given
            Jwt jwt = Jwt.withTokenValue("mock-token")
                    .header("alg", "none")
                    .claim("sub", "abc-123")
                    .claim("email", "user@test.pl")
                    .build();
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
}
