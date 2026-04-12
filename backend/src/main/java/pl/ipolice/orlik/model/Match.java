package pl.ipolice.orlik.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ipolice.orlik.model.enums.MatchType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchType matchType;

    @Column(name = "team_a_score")
    private Integer teamAScore;

    @Column(name = "team_b_score")
    private Integer teamBScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchParticipation> participations = new ArrayList<>();

    public void addParticipation(MatchParticipation participation) {
        participations.add(participation);
        participation.setMatch(this);
    }
}
