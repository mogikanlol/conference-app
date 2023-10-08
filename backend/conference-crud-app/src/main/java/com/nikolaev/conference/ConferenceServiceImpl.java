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
    private final ConferenceRoleListHolderRepository roleListHolderRepository;
    private final ConferenceRoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ConferenceUserRolesRepository conferenceUserRolesRepository;

    @Override
    public Page<BriefConferenceDto> getAll(Pageable pageable) {
        return conferenceRepository.findAll(pageable).map(ConferenceMapper::toBriefDto);
    }


    @Override
    public ConferenceDto getConference(Long id) throws ConferenceNotFoundException {
        Conference conference = conferenceRepository.getOne(id);
        if (conference == null)
            throw new ConferenceNotFoundException("Conference not found");
        else
            return ConferenceMapper.toDto(conference);
    }

    @Override
    public void createConference(ConferenceRequestDto request) {

    }

    public void createConference(ConferenceRequest request) {
        Conference conference = new Conference();
        User user = userRepository.findByUsername(request.getOrganizer().getUsername());
        conference.setTitle(request.getTitle());
        conference.setAcronym(request.getAcronym());
        conference.setWebPage(request.getWebPage());
        conference.setCity(request.getCity());
        conference.setCountry(request.getCountry());
        conference.setExpirationDate(request.getExpirationDate());



        ConferenceRole creator = roleRepository.findByName(ConferenceRoleName.CREATOR);

        conferenceRepository.save(conference);

        UserRoleInConf userRoleInConf = new UserRoleInConf();
        userRoleInConf.setConference(conference);
        userRoleInConf.setConferenceId(conference.getId());
        userRoleInConf.setUserId(user.getId());
        userRoleInConf.setUser(user);
        userRoleInConf.setRole(creator.getName().getValue() + 1);

        repo.save(userRoleInConf);
    }

    /*
    @Override
    public List<BriefUserDto> getReviewers(Long id) {
        Conference conference = conferenceRepository.getOne(id);
        List<User> userList = new ArrayList<>();
        for (ConferenceUserRoles conferenceUserRoles : conference.getConferenceUserRoles()) {
            List<ConferenceRoleName> roles =
                    conferenceUserRoles.getRoleList().getRoles().stream().map(ConferenceRole::getName).collect(Collectors.toList());
            if (roles.contains(ConferenceRoleName.REVIEWER))
                userList.add(conferenceUserRoles.getUser());
        }
        return userList.stream().map(UserMapper::toBriefDto).collect(Collectors.toList());
    }

     */

    private final UserRoleInConfRepo repo;

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

        /*
        ConferenceUserRoles userRoles = conferenceUserRolesRepository.findByConferenceIdAndUserId(conferenceId, userId);
        ConferenceRoleListHolder roleList = userRoles.getRoleList();


        Set<ConferenceRole> newRoleList =
                roles.stream().map(ConferenceRoleName::fromInt).map(roleRepository::findByName).collect(Collectors.toSet());
        roleList.setRoles(newRoleList);
        roleListHolderRepository.save(roleList);

        return UserMapper.toBriefRolesDto(userRepository.getOne(userId), conferenceId);

         */
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

/*
        Conference conference = conferenceRepository.getOne(conferenceId);
        for (ConferenceUserRoles userRoles : conference.getConferenceUserRoles()) {
            if (userRoles.getUser().getId().equals(userId)) {
                return UserMapper.toBriefRolesDto(userRoles.getUser(), conferenceId);
            }
        }
        return null;

 */
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
            /*

            ConferenceRoleListHolder listHolder = new ConferenceRoleListHolder();
            listHolder.setRoles(new HashSet<ConferenceRole>() {{
                add(roleRepository.findByName(ConferenceRoleName.SUBMITTER));
            }});
            roleListHolderRepository.save(listHolder);

            Conference conference = conferenceRepository.getOne(conferenceId);
            ConferenceUserRoles conferenceUserRoles = new ConferenceUserRoles();
            conferenceUserRoles.setConference(conference);
            conferenceUserRoles.setUser(user);
            conferenceUserRoles.setRoleList(listHolder);
            conferenceUserRolesRepository.save(conferenceUserRoles);

             */


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

    /*
    @Override
    public void addUsers(Long id, List<Long> users) {
        Conference conference = conferenceRepository.getOne(id);
        Set<User> userList = users.stream().map(userRepository::getOne).collect(Collectors.toSet());

        for (User user : userList) {
            ConferenceRoleListHolder listHolder = new ConferenceRoleListHolder();
            listHolder.setRoles(new HashSet<ConferenceRole>() {{
//                add(participant);
            }});

            ConferenceUserRoles userRoles = new ConferenceUserRoles();
            userRoles.setUser(user);
            userRoles.setConference(conference);
            userRoles.setRoleList(listHolder);

            roleListHolderRepository.save(listHolder);
            conferenceUserRolesRepository.save(userRoles);
        }

    }

     */

    /*
    @Override
    public List<BriefUserRolesDto> getUsers(Long id) {
        Conference conference = conferenceRepository.getOne(id);
        List<User> userList = new ArrayList<>();
        conference.getConferenceUserRoles().forEach(userRole -> userList.add(userRole.getUser()));
        return userList.stream().map(user -> UserMapper.toBriefRolesDto(user, id)).collect(Collectors.toList());
    }

     */

    /*
    @Override
    public void addReviewers(Long id, List<Long> reviewers) {
        List<User> userList = reviewers.stream().map(userRepository::getOne).collect(Collectors.toList());

        ConferenceRole reviewerRole = roleRepository.findByName(ConferenceRoleName.REVIEWER);
//        ConferenceRole participantRole = roleRepository.findByName(ConferenceRoleName.PARTICIPANT);

        for (User user : userList) {
            for (ConferenceUserRoles userRoles : user.getConferenceUserRoles()) {
                ConferenceRoleListHolder roleList = userRoles.getRoleList();
                Set<ConferenceRole> roles = roleList.getRoles();
                if (roles.contains(reviewerRole))
                    break;
//                roles.remove(participantRole);
                roles.add(reviewerRole);
                roleList.setRoles(roles);
                roleListHolderRepository.save(roleList);
            }
        }

    }

     */

    @Override
    public List<BriefSubmissionDto> getSubmissions(Long id, Pageable pageable) {
        return SubmissionMapper.toListBriefDto(conferenceRepository.getOne(id).getSubmissions());
    }


}
