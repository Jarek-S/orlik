package pl.ipolice.orlik.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import pl.ipolice.orlik.dto.MatchDto;
import pl.ipolice.orlik.dto.MatchSaveDto;
import pl.ipolice.orlik.service.MatchService;

@Controller
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @QueryMapping
    public MatchDto getMatch(@Argument Long id) {
        return matchService.getMatchById(id);
    }

    @MutationMapping
    public MatchDto createMatch(@Argument Long groupId, @Argument MatchSaveDto matchData) {
        return matchService.createMatch(groupId, matchData);
    }
}
