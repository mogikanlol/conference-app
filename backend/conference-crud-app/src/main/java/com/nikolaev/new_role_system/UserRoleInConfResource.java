package com.nikolaev.new_role_system;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRoleInConfResource {

    private final UserRoleInConfRepo repo;

    @GetMapping("/roles")
    public List<UserRoleInConf> getRoles(@RequestParam("userId") Long userId, @RequestParam("confId") Long conferenceId) {
        return repo.getByUserIdAndConferenceId(userId, conferenceId);
    }

}
