package com.nikolaev.user;


import com.nikolaev.conference.dto.BriefConferenceDto;
import com.nikolaev.conference_request.dto.BriefConferenceRequestDto;
import com.nikolaev.conference_request.dto.ConferenceRequestMapper;
import com.nikolaev.conference_role.ConferenceRoleName;
import com.nikolaev.conference_user_roles.ConferenceUserRolesRepository;
import com.nikolaev.new_role_system.UserRoleInConf;
import com.nikolaev.new_role_system.UserRoleInConfRepo;
import com.nikolaev.review.dto.ReviewDto;
import com.nikolaev.security.JwtTokenUtil;
import com.nikolaev.submission.Submission;
import com.nikolaev.submission.dto.BriefSubmissionDto;
import com.nikolaev.submission.dto.SubmissionMapper;
import com.nikolaev.submission_role.SubmissionRoleName;
import com.nikolaev.user.dto.BriefUserDto;
import com.nikolaev.user.dto.BriefUserRolesDto;
import com.nikolaev.user.dto.UserDto;
import com.nikolaev.user.dto.UserMapper;
import com.nikolaev.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Environment env;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;
    private final JwtTokenUtil jwtTokenUtil;
    private final ConferenceUserRolesRepository conferenceUserRolesRepository;


    @Override
    public UserDto save(UserDto user) {
        log.debug("in save()");
        return null;
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException("User not found");
        else
            return user;
    }


    public void sendConfirmationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome");
        message.setText("Welcome to website. Please, confirm your email.\n" + generateConfirmationUrl(user));
        emailSender.send(message);
    }

    @Override
    public User confirmEmail(String token) throws UserNotFoundException {
        User user = userRepository.findByConfirmationToken(token);

        if (user != null) {
            user.setConfirmationToken("");
            user.setConfirmed(true);
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<BriefConferenceDto> getUserConferences(Long id) {
        User user = userRepository.getOne(id);
        List<UserRoleInConf> test = user.getUserRoleInConfList();
        return test.stream()
                .map(UserRoleInConf::getConference)
                .distinct()
                .map(r -> {
                    return new BriefConferenceDto(r.getId(), r.getTitle(), r.getExpirationDate());
                }).toList();
        /*
        List<Conference> conferenceList = new ArrayList<>();
        user.getConferenceUserRoles().forEach(conferenceUserRoles -> conferenceList.add(conferenceUserRoles.getConference()));
        return ConferenceMapper.toListBriefDto(conferenceList);
         */
    }

    @Override
    public List<UserDto> getReviewers() {
        Authority authority = authorityRepository.findByName(AuthorityName.ROLE_REVIEWER);
        List<User> reviewers = userRepository.findByAuthority(authority.getId());
        return UserMapper.toListDto(reviewers);
    }

    @Override
    public List<ReviewDto> getReviewList(Long id, Long conferenceId) {
        return null;
//        if (conferenceId == null) {
//            return reviewMapper.toListDto(userRepository.getOne(id).getReviews());
//        } else {
//            return reviewMapper.toListDto(userRepository.getOne(id).getReviews().stream().filter(review ->
//                    review.getSubmission().getConference().getId().equals(conferenceId))
//                    .collect(Collectors.toList()));
//        }
    }

    /*
    @Override
    public boolean isReviewer(Long userId, Long conferenceId) {
        User user = userRepository.getOne(userId);
        for (ConferenceUserRoles conferenceUserRoles : user.getConferenceUserRoles()) {
            if (conferenceUserRoles.getConference().getId().equals(conferenceId)) {
                for (ConferenceRole role : conferenceUserRoles.getRoleList().getRoles()) {
                    if (role.getName().equals(ConferenceRoleName.REVIEWER))
                        return true;
                }
            }
        }
        return false;
    }
     */

    /*
    @Override
    public Set<ConferenceRole> getUser(Long id) {
        User user = userRepository.getOne(id);
        Set<ConferenceRole> list = new HashSet<>();
        for (ConferenceUserRoles conferenceUserRoles : user.getConferenceUserRoles()) {
            list = conferenceUserRoles.getRoleList().getRoles();
        }
        return list;
    }
     */


    @Override
    public List<BriefSubmissionDto> getSubmissions(Long userId) {
        User user = userRepository.getOne(userId);
        List<Submission> submissions = new ArrayList<>();
        user.getSubmissionUserRoles().forEach(submissionUserRoles -> submissions.add(submissionUserRoles.getSubmission()));
        return SubmissionMapper.toListBriefDto(submissions);
    }

    @Override
    public Page<BriefUserDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toBriefDto);
    }

    @Override
    public List<BriefConferenceRequestDto> getConferenceRequests(Long id) {
        return ConferenceRequestMapper.toListBriefDto(userRepository.getOne(id).getRequests());
    }

    private final UserRoleInConfRepo repo;

    @Override
    public Page<BriefUserRolesDto> findUsersByConferenceId(Long id, Integer roleNumber, Pageable pageable) {
        if (roleNumber != null) {
            return userRepository
                    .findAllByConferenceIdAndRoleName(id, ConferenceRoleName.fromInt(roleNumber), pageable)
                    .map(user -> UserMapper.toBriefRolesDto(user, id));
        }
        else
            return userRepository
                    .findAllByConferenceId(id, pageable)
                    .map(user -> UserMapper.toBriefRolesDto(user, id));
    }

    @Override
    public Page<BriefUserDto> getReviewersBySubmissionId(Long submissionId, Pageable pageable) {
        return userRepository.findAllBySubmissionIdAndRoleName(submissionId, SubmissionRoleName.REVIEWER, pageable)
                .map(UserMapper::toBriefDto);
    }

    @Override
    public Page<BriefUserRolesDto> searchUsersByConferenceId(Long id, Integer roleNumber, String searchString, Pageable pageable) {
        return userRepository.searchByConferenceIdAndRoleName(id, roleNumber + 1, searchString, pageable)
                .map(user -> UserMapper.toBriefRolesDto(user, id));
        /*
        return userRepository.searchByConferenceIdAndRoleName(id, ConferenceRoleName.fromInt(roleNumber), searchString, pageable)
                .map(user -> UserMapper.toBriefRolesDto(user, id));

         */
    }

    @Override
    public Page<BriefUserDto> searchUsers(String searchString, Pageable pageable) {
        return userRepository.searchAll(searchString, pageable).map(UserMapper::toBriefDto);
    }

    private String generateConfirmationUrl(User user) {
        String host = env.getProperty("host");
        return host + "/confirmation/" + user.getConfirmationToken();
    }


}
