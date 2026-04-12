package pl.ipolice.orlik;

import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import pl.ipolice.orlik.model.Group;
import pl.ipolice.orlik.model.Player;
import pl.ipolice.orlik.model.User;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.PlayerRepository;
import pl.ipolice.orlik.repository.UserRepository;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
public abstract class BaseIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected GroupRepository groupRepository;

    private static final String REALM_IMPORT_FILE = "realm-export.json";
    private static final String REALM_TREE_NODE = "realm";
    private static final String SECURITY_RESOURCE_REGISTRY = "spring.security.oauth2.resourceserver.jwt.issuer-uri";

    protected User defaultOwner;
    protected Group defaultGroup;

    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:latest")
            .withRealmImportFile(REALM_IMPORT_FILE); // it has to be in src/test/resources!

    static {
        postgres.start();
        keycloak.start();
    }

    @DynamicPropertySource
    static void registerResourceServerIssuer(DynamicPropertyRegistry registry) {
        registry.add(SECURITY_RESOURCE_REGISTRY,
                () -> keycloak.getAuthServerUrl() + getRealmNameFromJson());
    }

    protected void baseSetup() {
        defaultOwner = new User();
        defaultOwner.setKeycloakId(UUID.randomUUID().toString());
        defaultOwner.setEmail("test-owner@orlik.pl");
        defaultOwner.setCreatedAt(LocalDate.now());
        defaultOwner = userRepository.save(defaultOwner);

        defaultGroup = new Group();
        defaultGroup.setName("Default test group");
        defaultGroup.setOwner(defaultOwner);
        defaultGroup.setCreatedAt(LocalDate.now());
        defaultGroup = groupRepository.save(defaultGroup);
    }

    protected Player createPlayer(String firstName, String nickName) {
        Player player = new Player();
        player.setFirstName(firstName);
        player.setNickName(nickName);
        player.setGroup(defaultGroup);
        player.setJoinedAt(LocalDate.now());
        return playerRepository.save(player);
    }

    private static String getRealmNameFromJson() {
        try {
            InputStream is = BaseIntegrationTest.class.getClassLoader()
                    .getResourceAsStream(REALM_IMPORT_FILE);

            return new ObjectMapper().readTree(is).get(REALM_TREE_NODE).asText();
        } catch (Exception e) {
            throw new RuntimeException("Realm name extraction from JSON failed.", e);
        }
    }
}
