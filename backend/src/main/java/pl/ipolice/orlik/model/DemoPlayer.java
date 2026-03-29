package pl.ipolice.orlik.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.ipolice.orlik.model.enums.PrimaryPosition;

@Entity
@Table(name = "demo_players")
@Getter
@Setter
public class DemoPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int speedRank;
    private int techniqueRank;
    @Enumerated(EnumType.STRING)
    private PrimaryPosition primaryPosition;
}
