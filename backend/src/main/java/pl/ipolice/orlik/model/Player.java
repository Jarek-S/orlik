package pl.ipolice.orlik.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ipolice.orlik.model.enums.PrimaryPosition;

import java.time.LocalDate;

@Entity
@Table(name = "players", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "group_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "speed_rank")
    private Integer speedRank;

    @Column(name = "technique_rank")
    private Integer techniqueRank;

    @Column(name = "defense_skill_rank")
    private Integer defenseSkillRank;

    @Column(name = "defense_work_rate")
    private Integer defenseWorkRate;

    @Column(name = "stamina")
    private Integer stamina;

    @Column(name = "primary_position")
    @Enumerated(EnumType.STRING)
    private PrimaryPosition primaryPosition;

    @Column(name = "can_play_as_gk")
    private boolean canPlayAsGk = false;

    @Column(name = "is_goalkeeper_today")
    private boolean isGoalkeeperToday = false;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    @Column(name = "is_coach", nullable = false)
    private boolean isCoach = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(name = "joined_at", updatable = false)
    private LocalDate joinedAt;
}
