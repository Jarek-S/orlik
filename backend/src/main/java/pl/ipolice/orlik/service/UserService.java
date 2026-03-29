package pl.ipolice.orlik.service;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import pl.ipolice.orlik.model.Group;
import pl.ipolice.orlik.model.User;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.PlayerRepository;
import pl.ipolice.orlik.repository.UserRepository;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final GroupRepository groupRepository;

    public User getOrPrepareUser(Jwt jwt) {
        String keycloakId = jwt.getClaim("keycloakId");

        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setKeycloakId(keycloakId);
                    newUser.setEmail(jwt.getClaim("email"));
                    return newUser;
                });
    }

    @Transactional
    public User synchronizeUser(Jwt jwt, String inviteCode) {
        User user = userRepository.findByKeycloakId(jwt.getSubject()).orElse(null);

        if (StringUtils.isNotBlank(inviteCode)) {
            return processInviteCode(jwt, inviteCode, user);
        } else if (Objects.isNull(user)) {
            return registerUser(jwt);
        }
        return user;
    }

    private User processInviteCode(Jwt jwt, String inviteCode, User user) {
        return null;
    }

    private User registerUser(Jwt jwt) {
        LocalDate creationDate = LocalDate.now();
        User newUser = new User();
        newUser.setKeycloakId(jwt.getSubject());
        newUser.setEmail(jwt.getClaimAsString("email"));
        newUser.setCreatedAt(creationDate);

        User savedUser = userRepository.save(newUser);

        Group defaultGroup = new Group();
        defaultGroup.setCreatedAt(creationDate);
        defaultGroup.setOwner(savedUser);
        groupRepository.save(defaultGroup);

        return savedUser;
    }
}
