package com.nikolaev.submission.dto;

import com.nikolaev.document.dto.DocumentMapper;
import com.nikolaev.submission.Submission;
import com.nikolaev.submission.status.SubmissionStatus;
import com.nikolaev.submission_role.SubmissionRoleName;
import com.nikolaev.submission_user_roles.SubmissionUserRoles;
import com.nikolaev.user.User;
import com.nikolaev.user.dto.BriefUserDto;
import com.nikolaev.user.dto.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SubmissionMapper {
    private static final Logger logger = LoggerFactory.getLogger(SubmissionMapper.class);

    private SubmissionMapper() {
    }

    public static SubmissionDto toDto(Submission entity) {
        User author1 = entity.getAuthor();
        BriefUserDto author = new BriefUserDto(
                author1.getId(),
                author1.getUsername(),
                author1.getEmail(),
                author1.getFirstname(),
                author1.getLastname()
        );

        List<BriefUserDto> reviewers = entity.getTest().stream()
                .filter(r -> r.getRole().equals(SubmissionRoleName.REVIEWER))
                .map(r -> r.getUser())
                .distinct()
                .map(r -> {
                    return new BriefUserDto(r.getId(), r.getUsername(), r.getEmail(), r.getFirstname(), r.getLastname());
                }).toList();

        return new SubmissionDto(
                entity.getId(),
                entity.getTitle(),
                entity.isReviewable(),
                DocumentMapper.toListDto(entity.getDocuments()),
                mapStatus(entity.getStatus()),
                author,
//                mapAuthor(entity),
                reviewers,
//                mapReviewers(entity),
                entity.getConference().getId()
        );
    }

    private static int mapStatus(SubmissionStatus status) {
        return status.getName().getValue();
    }

    public static BriefSubmissionDto toBriefDto(Submission entity) {
        User author1 = entity.getAuthor();
        BriefUserDto author = new BriefUserDto(
                author1.getId(),
                author1.getUsername(),
                author1.getEmail(),
                author1.getFirstname(),
                author1.getLastname()
        );

        List<BriefUserDto> reviewers = entity.getTest().stream()
                .filter(r -> r.getRole().equals(SubmissionRoleName.REVIEWER))
                .map(r -> r.getUser())
                .distinct()
                .map(r -> {
                    return new BriefUserDto(r.getId(), r.getUsername(), r.getEmail(), r.getFirstname(), r.getLastname());
                }).toList();

        return new BriefSubmissionDto(
                entity.getId(),
                entity.getTitle(),
                author,
//                mapAuthor(entity),
                entity.getStatus().getName().getValue(),
                reviewers
//                mapReviewers(entity)
        );
    }

    public static List<SubmissionDto> toListDto(List<Submission> conferences) {
        return conferences.stream().map(SubmissionMapper::toDto).collect(Collectors.toList());
    }

    public static List<BriefSubmissionDto> toListBriefDto(List<Submission> conferences) {
        return conferences.stream().map(SubmissionMapper::toBriefDto).collect(Collectors.toList());
    }

/*
    private static BriefUserDto mapAuthor(Submission entity) {
        List<SubmissionUserRoles> userRoles = entity.getSubmissionUserRoles();
        for (SubmissionUserRoles userRole : userRoles) {
            if (userRole.getRole().getName().equals(SubmissionRoleName.AUTHOR))
                return UserMapper.toBriefDto(userRole.getUser());
        }
        return null;
    }

 */

    /*
    private static List<BriefUserDto> mapReviewers(Submission entity) {
        List<SubmissionUserRoles> userRoles = entity.getSubmissionUserRoles();
        List<BriefUserDto> reviewers = new ArrayList<>();
        for (SubmissionUserRoles userRole : userRoles) {
            if (userRole.getRole().getName().equals(SubmissionRoleName.REVIEWER))
                reviewers.add(UserMapper.toBriefDto(userRole.getUser()));
        }
        return reviewers;
    }

     */
}
