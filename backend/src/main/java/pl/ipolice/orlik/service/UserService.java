package pl.ipolice.orlik.service;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pl.ipolice.orlik.model.Group;
import pl.ipolice.orlik.model.Invitation;
import pl.ipolice.orlik.model.Player;
import pl.ipolice.orlik.model.User;
import pl.ipolice.orlik.model.enums.InvitationStatus;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.InvitationRepository;
import pl.ipolice.orlik.repository.PlayerRepository;
import pl.ipolice.orlik.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final GroupRepository groupRepository;
    private final InvitationRepository invitationRepository;


    @Transactional
    public User synchronizeUser(Jwt jwt, String inviteCode) {
        Optional<User> user = userRepository.findByKeycloakId(jwt.getSubject());

        if (StringUtils.isNotBlank(inviteCode)) {
            return handleInvitation(jwt, inviteCode, user);
        }

        return user.orElseGet(() -> registerUserWithDefaultGroup(jwt));
    }

    private User handleInvitation(Jwt jwt, String inviteCode, Optional<User> user) {
        Invitation invitation = invitationRepository.findByCode(UUID.fromString(inviteCode))
                .orElseThrow(() -> new RuntimeException("Invalid invitation code"));

        if (invitation.getStatus() == InvitationStatus.ACCEPTED) {
            throw new RuntimeException("Invitation already used.");
        }

        if (invitation.isExpired()) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            invitationRepository.save(invitation);
            throw new RuntimeException("Invitation expired!");
        }

        User invitedUser = user.orElseGet(() -> registerUser(jwt));

        assignUserToGroup(invitedUser, invitation.getGroup(), invitation.getPlayerName());

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);

        return invitedUser;
    }

    private User registerUserWithDefaultGroup(Jwt jwt) {
        User newUser = registerUser(jwt);

        Group defaultGroup = new Group();
        defaultGroup.setCreatedAt(newUser.getCreatedAt());
        defaultGroup.setOwner(newUser);
        groupRepository.save(defaultGroup);

        return newUser;
    }

    private User registerUser(Jwt jwt) {
        LocalDate creationDate = LocalDate.now();
        User newUser = new User();
        newUser.setKeycloakId(jwt.getSubject());
        newUser.setEmail(jwt.getClaimAsString("email"));
        newUser.setCreatedAt(creationDate);

        return userRepository.save(newUser);
    }

    private void assignUserToGroup(User user, Group group, String playerName) {
        Player newPlayer = new Player();
        newPlayer.setUser(user);
        newPlayer.setGroup(group);
        newPlayer.setFirstName(playerName);
        newPlayer.setJoinedAt(LocalDate.now());
        playerRepository.save(newPlayer);
    }
}
