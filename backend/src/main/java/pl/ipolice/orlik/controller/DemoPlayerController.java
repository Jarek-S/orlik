package pl.ipolice.orlik.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import pl.ipolice.orlik.model.DemoPlayer;
import pl.ipolice.orlik.repository.DemoPlayerRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DemoPlayerController {

    private final DemoPlayerRepository demoPlayerRepository;

    @QueryMapping
    public List<DemoPlayer> getDemoPlayers() {
        return demoPlayerRepository.findAll();
    }
}