package com.nikolaev.conference;

import com.nikolaev.conference.dto.BriefConferenceDto;
import com.nikolaev.conference.dto.ConferenceDto;
import com.nikolaev.conference.exception.ConferenceNotFoundException;
import com.nikolaev.conference_role.ConferenceRoleName;
import com.nikolaev.domain.ApiError;
import com.nikolaev.submission.SubmissionService;
import com.nikolaev.submission.dto.BriefSubmissionDto;
import com.nikolaev.submission.dto.SubmissionDto;
import com.nikolaev.user.UserService;
import com.nikolaev.user.dto.BriefUserRolesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;


@RestController
@RequestMapping("/api/conferences")
@RequiredArgsConstructor
public class ConferenceResource {

    private final ConferenceService conferenceService;

    private final SubmissionService submissionService;

    private final UserService userService;

    @GetMapping
    public Page<BriefConferenceDto> getConferences(@RequestParam(name = "status", required = false) Integer statusNumber,
                                                   @PageableDefault Pageable pageable) {
        if (statusNumber == null)
            return conferenceService.getAll(pageable);
        else
            return conferenceService.findAllByStatus(statusNumber, pageable);
    }

    @GetMapping("{id}")
    public ConferenceDto getConference(@PathVariable("id") Long id) throws ConferenceNotFoundException {
        return conferenceService.getConference(id);
    }

    @GetMapping("{id}/submissions")
    public Page<BriefSubmissionDto> getSubmissions(@PathVariable("id") Long id, @PageableDefault Pageable pageable,
                                                   @RequestParam(name = "status", required = false) Integer statusNumber) {
        if (statusNumber == null)
            return submissionService.findSubmissionsByConferenceId(id, pageable);
        else
            return submissionService.findSubmissionsByConferenceIdAndStatus(id, statusNumber, pageable);
    }

    @GetMapping("{conferenceId}/submissions/users/{userId}")
    public Page<BriefSubmissionDto> getUserSubmissions(@PathVariable("conferenceId") Long conferenceId,
                                                       @PathVariable("userId") Long userId,
                                                       @RequestParam(name = "status", required = false) Integer statusNumber,
                                                       @PageableDefault Pageable pageable) {
        if (statusNumber == null)
            return submissionService.getUserSubmissionsByConferenceId(conferenceId, userId, pageable);
        else
            return submissionService.getUserSubmissionsByConferenceIdAndStatus(conferenceId, userId, statusNumber, pageable);
    }

    @GetMapping("{conferenceId}/submissions/reviewers/{reviewerId}")
    public Page<BriefSubmissionDto> getReviewerSubmissions(@PathVariable("conferenceId") Long conferenceId,
                                                           @PathVariable("reviewerId") Long reviewerId,
                                                           @RequestParam(name = "status", required = false) Integer statusNumber,
                                                           @PageableDefault Pageable pageable) {
        if (statusNumber == null)
            return submissionService.getReviewerSubmissionsByConferenceId(conferenceId, reviewerId, pageable);
        else
            return submissionService.getReviewerSubmissionsByConferenceIdAndStatus(conferenceId, reviewerId, statusNumber, pageable);
    }

    @GetMapping("{id}/users")
    public Page<BriefUserRolesDto> getUsers(@PathVariable("id") Long id,
                                            @RequestParam(name = "role", required = false) Integer roleNumber,
                                            @RequestParam(name = "search", required = false) String searchString,
                                            @PageableDefault Pageable pageable) {
        if (searchString == null) {
            return userService.findUsersByConferenceId(id, roleNumber, pageable);
        } else if (!searchString.isEmpty()) {
            return userService.searchUsersByConferenceId(id, roleNumber, searchString, pageable);
        }
        return null;
    }

    /*
    @RequestMapping(value = "{id}/reviewers", method = RequestMethod.POST)
    public void addReviewers(@PathVariable("id") Long id, @RequestBody List<Long> reviewers) {
        conferenceService.addReviewers(id, reviewers);
    }

     */

    @GetMapping("{id}/reviewers")
    public Page<BriefUserRolesDto> getReviewers(@PathVariable("id") Long id,
                                                @PageableDefault Pageable pageable) {
        return userService.findUsersByConferenceId(id, ConferenceRoleName.REVIEWER.getValue(), pageable);
    }

    /**
     * Creates the new submission with new document
     *
     * @param submission submission request object
     * @param file       document file
     * @param id         conference id
     */
    @PostMapping(path = "{id}/submissions", consumes = {"multipart/form-data"})
    public void submissionUpload(@RequestPart("submission") SubmissionDto submission,
                                 @RequestPart("file") MultipartFile file,
                                 @PathVariable("id") Long id) throws IOException {
        submissionService.save(file, submission, id);

    }

    @PutMapping("{conferenceId}/users/{userId}")
    public BriefUserRolesDto changeRoles(@PathVariable("conferenceId") Long conferenceId,
                                         @PathVariable("userId") Long userId,
                                         @RequestBody Set<Integer> roles) {
        return conferenceService.changeRoles(conferenceId, userId, roles);
    }

    @GetMapping("{conferenceId}/users/{userId}")
    public BriefUserRolesDto getUserRoles(@PathVariable("conferenceId") Long conferenceId,
                                          @PathVariable("userId") Long userId) {
        return conferenceService.getUserRoles(conferenceId, userId);
    }

    @PostMapping("{conferenceId}/invite")
    public void inviteUser(@PathVariable("conferenceId") Long conferenceId, @RequestBody String username) {
        conferenceService.inviteUser(conferenceId, username);
    }

    @GetMapping("{conferenceId}/statistic")
    public ConferenceStatistic getStatistic(@PathVariable("conferenceId") Long conferenceId) {
        return conferenceService.getConferenceStatistic(conferenceId);
    }

    @ExceptionHandler(ConferenceNotFoundException.class)
    public ResponseEntity<ApiError> handleConferenceNotFoundException(Exception e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                e.getMessage(),
                e.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
