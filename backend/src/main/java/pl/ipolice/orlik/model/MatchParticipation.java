package pl.ipolice.orlik.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ipolice.orlik.model.enums.Team;

@Entity
@Table(name = "match_participations")
@Getter
@Setter
@NoArgsConstructor
public class MatchParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Team team;

    @Column(nullable = false)
    private int goals = 0;

    @Column(nullable = false)
    private int assists = 0;

    @Column(nullable = false)
    private boolean isMvp = false;

    @Column(nullable = false)
    private boolean playedAsGoalkeeper = false;
}
