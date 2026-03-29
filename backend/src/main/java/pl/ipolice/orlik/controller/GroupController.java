package pl.ipolice.orlik.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.ipolice.orlik.service.GroupService;

@Controller
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

}
