package com.nikolaev.new_role_system;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleInConfRepo extends JpaRepository<UserRoleInConf, Long> {

    List<UserRoleInConf> getByUserIdAndConferenceId(Long userId, Long conferenceId);

    void deleteByUserIdAndConferenceIdAndRole(Long userId, Long conferenceId, Integer role);
}
