package pl.ipolice.orlik.config;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pl.ipolice.orlik.model.Group;
import pl.ipolice.orlik.model.Player;
import pl.ipolice.orlik.model.User;
import pl.ipolice.orlik.model.enums.PrimaryPosition;
import pl.ipolice.orlik.repository.DemoPlayerRepository;
import pl.ipolice.orlik.repository.GroupRepository;
import pl.ipolice.orlik.repository.PlayerRepository;
import pl.ipolice.orlik.repository.UserRepository;

import java.util.List;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final PlayerRepository playerRepository;
    private final DemoPlayerRepository demoPlayerRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    private final Group g1 = createG("Orlik wtorek", "Paproci", "OR_P", 1L);
    private final Group g2 = createG("Orlik czwartek", "Świętoborzycóœ", "OR_S", 1L);

    private final List<Player> testPlayers = List.of(
            createP("Mariusz Batory", 75, 60, 80, 70, 75, PrimaryPosition.DEF, true, false, g1),
            createP("Kacper Zając", 82, 90, 40, 60, 80, PrimaryPosition.ATT, false, false, g1),
            createP("Jarosław Kowalski", 40, 45, 85, 90, 70, PrimaryPosition.DEF, false, false, g1),
            createP("Tomek Nowak", 65, 70, 60, 65, 60, PrimaryPosition.MID, false, false, g1),
            createP("Artur Król", 88, 75, 50, 40, 65, PrimaryPosition.ATT, false, false, g1),
            createP("Sebastian Mila", 92, 50, 40, 30, 55, PrimaryPosition.MID, false, false, g1),
            createP("Michał Gibała", 55, 95, 45, 85, 90, PrimaryPosition.ATT, false, false, g1),
            createP("Robert Lewy", 70, 65, 70, 75, 80, PrimaryPosition.MID, false, false, g1),
            createP("Łukasz Testowy", 60, 50, 90, 40, 70, PrimaryPosition.GK, true, true, g1), // Bramkarz
            createP("Paweł Defensywny", 30, 40, 95, 95, 85, PrimaryPosition.DEF, false, false, g1),
            createP("Szymon Turbiński", 60, 85, 50, 90, 85, PrimaryPosition.MID, false, false, g1),
            createP("Rafał Księgowy", 45, 40, 60, 50, 45, PrimaryPosition.DEF, false, false, g1),
            createP("Adam Manager", 55, 55, 55, 60, 60, PrimaryPosition.MID, false, false, g1),
            createP("Janek IT", 70, 70, 40, 40, 50, PrimaryPosition.ATT, false, false, g1),
            createP("Darek Logistyk", 50, 65, 75, 80, 75, PrimaryPosition.DEF, true, false, g1),
            createP("Marek Sprzedawca", 75, 80, 30, 40, 70, PrimaryPosition.ATT, false, false, g1),
            createP("Piotr Prezes", 85, 40, 30, 20, 35, PrimaryPosition.ATT, false, false, g1),
            createP("Kuba Junior", 65, 88, 45, 95, 90, PrimaryPosition.MID, false, false, g1),
            createP("Grzegorz Beton", 40, 35, 88, 45, 50, PrimaryPosition.GK, true, true, g1), // Bramkarz
            createP("Andrzej Kadrowy", 50, 50, 65, 70, 65, PrimaryPosition.DEF, false, false, g1),
            createP("Wiesław Legenda", 90, 30, 40, 20, 25, PrimaryPosition.MID, false, false, g2),
            createP("Młody Zdolny", 75, 95, 50, 85, 95, PrimaryPosition.ATT, false, false, g2),
            createP("Krzysiek Mur", 40, 50, 92, 80, 85, PrimaryPosition.DEF, false, false, g2),
            createP("Jacek Placuś", 55, 60, 55, 55, 55, PrimaryPosition.MID, false, false, g2),
            createP("Marcin Skrzydło", 65, 85, 40, 70, 75, PrimaryPosition.ATT, false, false, g2),
            createP("Stefan Przecinak", 35, 75, 85, 95, 90, PrimaryPosition.DEF, false, false, g2),
            createP("Bolek Atak", 80, 80, 30, 40, 65, PrimaryPosition.ATT, false, false, g2),
            createP("Lolek Pomoc", 70, 70, 70, 70, 70, PrimaryPosition.MID, false, false, g2),
            createP("Władek Rękawica", 50, 40, 85, 40, 60, PrimaryPosition.GK, true, true, g2), // Bramkarz
            createP("Tomek Technik", 95, 60, 35, 45, 60, PrimaryPosition.MID, false, false, g2),
            createP("Szymon Turbiński", 60, 85, 50, 90, 85, PrimaryPosition.MID, false, false, g2),
            createP("Rafał Księgowy", 45, 40, 60, 50, 45, PrimaryPosition.DEF, false, false, g2),
            createP("Adam Manager", 55, 55, 55, 60, 60, PrimaryPosition.MID, false, false, g2),
            createP("Janek IT", 70, 70, 40, 40, 50, PrimaryPosition.ATT, false, false, g2),
            createP("Darek Logistyk", 50, 65, 75, 80, 75, PrimaryPosition.DEF, true, false, g2),
            createP("Marek Sprzedawca", 75, 80, 30, 40, 70, PrimaryPosition.ATT, false, false, g2),
            createP("Piotr Prezes", 85, 40, 30, 20, 35, PrimaryPosition.ATT, false, false, g2),
            createP("Kuba Junior", 65, 88, 45, 95, 90, PrimaryPosition.MID, false, false, g2),
            createP("Grzegorz Beton", 40, 35, 88, 45, 50, PrimaryPosition.GK, true, true, g2), // Bramkarz
            createP("Andrzej Kadrowy", 50, 50, 65, 70, 65, PrimaryPosition.DEF, false, false, g2)
    );


    public DataInitializer(PlayerRepository playerRepository, GroupRepository groupRepository, UserRepository userRepository, JdbcTemplate jdbcTemplate, DemoPlayerRepository demoPlayerRepository) {
        this.playerRepository = playerRepository;
        this.demoPlayerRepository = demoPlayerRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        System.out.println(">>> Starting database cleanup and loading test data...");

        // Clean table to start id == 1
//        jdbcTemplate.execute("TRUNCATE TABLE players RESTART IDENTITY CASCADE");
//
//        User user = createUser("jarek@test.pl", "xxxxx");
//        userRepository.save(user);
//
//        groupRepository.save(g1);
//        groupRepository.save(g2);
//        initializeTestPlayers();


        System.out.println(">>> Database initialization completed successfully.");
    }


    private void initializeTestPlayers() {
        playerRepository.saveAll(testPlayers);
    }

    private Player createP(String name, int tech, int speed, int defRank, int work, int stam,
                           PrimaryPosition pos, boolean canGK, boolean isGK,
                           Group group) {
        Player p = new Player();
        p.setFirstName(name);
        p.setTechniqueRank(tech);
        p.setSpeedRank(speed);
        p.setDefenseSkillRank(defRank);
        p.setDefenseWorkRate(work);
        p.setStamina(stam);
        p.setPrimaryPosition(pos);
        p.setCanPlayAsGk(canGK);
        p.setIsGoalkeeperToday(isGK);


        return p;
    }

    private Group createG(String name, String location, String inviteCode, Long ownerId) {
        Group group = new Group();
        group.setName(name);
        return group;
    }

    private User createUser(String email, String keycloakId) {
        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setEmail(email);
        return user;
    }
}
