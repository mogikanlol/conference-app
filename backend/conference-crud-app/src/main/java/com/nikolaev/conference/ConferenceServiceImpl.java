package com.nikolaev.conference;

import com.nikolaev.conference.dto.BriefConferenceDto;
import com.nikolaev.conference.dto.ConferenceDto;
import com.nikolaev.conference.dto.ConferenceMapper;
import com.nikolaev.conference.exception.ConferenceNotFoundException;
import com.nikolaev.conference_request.ConferenceRequest;
import com.nikolaev.conference_request.dto.ConferenceRequestDto;
import com.nikolaev.conference_role.*;
import com.nikolaev.conference_user_roles.ConferenceUserRoles;
import com.nikolaev.conference_user_roles.ConferenceUserRolesRepository;
import com.nikolaev.new_role_system.UserRoleInConf;
import com.nikolaev.new_role_system.UserRoleInConfRepo;
import com.nikolaev.submission.dto.BriefSubmissionDto;
import com.nikolaev.submission.dto.SubmissionMapper;
import com.nikolaev.user.User;
import com.nikolaev.user.UserRepository;
import com.nikolaev.user.dto.BriefUserDto;
import com.nikolaev.user.dto.BriefUserRolesDto;
import com.nikolaev.user.dto.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final ConferenceRoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserRoleInConfRepo repo;

    @Override
    public Page<BriefConferenceDto> getAll(Pageable pageable) {
        return conferenceRepository.findAll(pageable)
                .map(ConferenceMapper::toBriefDto);
    }

    @Override
    public ConferenceDto getConference(Long id) throws ConferenceNotFoundException {
        return conferenceRepository.findById(id)
                .map(ConferenceMapper::toDto)
                .orElseThrow(() -> new ConferenceNotFoundException("Conference not found"));
    }

    @Override
    public void createConference(ConferenceRequest request) {
        User user = userRepository.findByUsername(request.getOrganizer().getUsername());
        ConferenceRole creator = roleRepository.findByName(ConferenceRoleName.CREATOR);

        Conference conference = new Conference();
        conference.setTitle(request.getTitle());
        conference.setAcronym(request.getAcronym());
        conference.setWebPage(request.getWebPage());
        conference.setCity(request.getCity());
        conference.setCountry(request.getCountry());
        conference.setExpirationDate(request.getExpirationDate());

        conferenceRepository.save(conference);

        UserRoleInConf userRoleInConf = new UserRoleInConf();
        userRoleInConf.setConference(conference);
        userRoleInConf.setConferenceId(conference.getId());
        userRoleInConf.setUserId(user.getId());
        userRoleInConf.setUser(user);
        userRoleInConf.setRole(creator.getName().getValue() + 1);

        repo.save(userRoleInConf);
    }

    @Override
    public BriefUserRolesDto changeRoles(Long conferenceId, Long userId, Set<Integer> roles1) {
        Set<Integer> roles = roles1.stream()
                .map(x -> x + 1)
                .collect(Collectors.toSet());

        List<UserRoleInConf> list = repo.getByUserIdAndConferenceId(userId, conferenceId);

        // delete orphans
        List<UserRoleInConf> forDeletion = list.stream()
                .filter(x -> !roles.contains(x.getRole()))
                .toList();

        if (!forDeletion.isEmpty()) {
            repo.deleteAll(forDeletion);
        }

        // add new
        Set<Integer> existingRoles = list.stream()
                .map(UserRoleInConf::getRole)
                .filter(roles::contains)
                .collect(Collectors.toSet());

        Set<Integer> newRoles = roles.stream()
                .filter(r -> !existingRoles.contains(r))
                .collect(Collectors.toSet());

        List<UserRoleInConf> forAddition = newRoles.stream()
                .map(r -> {
                    UserRoleInConf entity = new UserRoleInConf();
                    entity.setRole(r);
                    entity.setUserId(userId);
                    entity.setConferenceId(conferenceId);
                    return entity;
                }).toList();
        repo.saveAll(forAddition);

        User user = userRepository.getOne(userId);
        List<Integer> asd = new ArrayList<>();
        asd.addAll(existingRoles);
        asd.addAll(newRoles);
        asd = asd.stream().map(r->r -1).toList();
        BriefUserRolesDto dto = new BriefUserRolesDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                asd
        );
        return dto;
    }

    @Override
    public BriefUserRolesDto getUserRoles(Long conferenceId, Long userId) {
        List<UserRoleInConf> list = repo.getByUserIdAndConferenceId(userId, conferenceId);
        User user = userRepository.getOne(userId);
        BriefUserRolesDto dto = new BriefUserRolesDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                list.stream().map(r -> r.getRole() - 1).toList()
        );
        return dto;
    }

    @Override
    public void inviteUser(Long conferenceId, String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            if (user.getConferenceUserRoles()
                    .stream()
                    .anyMatch(conferenceUserRoles -> conferenceUserRoles.getConference().getId().equals(conferenceId))) {
                return;
            }

            UserRoleInConf userRoleInConf = new UserRoleInConf();
            userRoleInConf.setConferenceId(conferenceId);
            userRoleInConf.setUserId(user.getId());
            userRoleInConf.setRole(ConferenceRoleName.SUBMITTER.getValue() + 1);

            repo.save(userRoleInConf);
        }
    }

    @Override
    public Page<BriefConferenceDto> findAllByStatus(Integer statusNumber, Pageable pageable) {
        if (statusNumber.equals(0)) {
            return conferenceRepository.findAllActive(new Date(), pageable).map(ConferenceMapper::toBriefDto);
        } else if (statusNumber.equals(1)) {
            return conferenceRepository.findAllCompleted(new Date(), pageable).map(ConferenceMapper::toBriefDto);
        }
        return null;
    }

    @Override
    public ConferenceStatistic getConferenceStatistic(Long conferenceId) {
        return new ConferenceStatistic(conferenceRepository.getOne(conferenceId));
    }
}
