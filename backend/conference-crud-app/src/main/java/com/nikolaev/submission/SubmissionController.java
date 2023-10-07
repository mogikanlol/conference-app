package com.nikolaev.submission;

import com.nikolaev.document.dto.DocumentDto;
import com.nikolaev.submission.dto.SubmissionDto;
import com.nikolaev.user.UserService;
import com.nikolaev.user.dto.BriefUserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "api")
@RequiredArgsConstructor
public class SubmissionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SubmissionService submissionService;
    private final UserService userService;


    /**
     * Returns submission by id
     *
     * @param submissionId Submission id
     * @return Submission
     */
    @RequestMapping(value = "/submissions/{submissionId}", method = RequestMethod.GET)
    public SubmissionDto getSubmission(@PathVariable("submissionId") Long submissionId) {
        logger.info("in getSubmission()");
        return submissionService.getSubmission(submissionId);
    }

    /**
     * Returns all submission
     *
     * @return Submissions from specific conference
     */
    @RequestMapping(value = "/submissions", method = RequestMethod.GET)
    public List<SubmissionDto> getAll(@RequestParam(value = "reviewable", required = false) boolean reviewable) {
        return submissionService.getAll(reviewable);
    }


    /**
     * @param submissionId
     * @param reviewable
     * @return
     */
    @RequestMapping(value = "/submissions/{submissionId}/reviewable", method = RequestMethod.PUT)
    public SubmissionDto setReviewable(@PathVariable("submissionId") Long submissionId,
                                        @RequestBody String reviewable) {
        return submissionService.setOnReview(submissionId, Boolean.valueOf(reviewable));
    }

    /**
     * Adds reviewers to existing submission
     *
     * @param submissionId Submission id
     * @param reviewers    List reviewers
     */
    @RequestMapping(value = "/submissions/{submissionId}/reviewers", method = RequestMethod.POST)
    public void addReviewers(@PathVariable("submissionId") Long submissionId, @RequestBody List<Long> reviewers) {
        submissionService.addReviewers(submissionId, reviewers);
    }

    @RequestMapping(value = "/submissions/{submissionId}/reviewers", method = RequestMethod.GET)
    public Page<BriefUserDto> getReviewers(@PathVariable("submissionId") Long submissionId, @PageableDefault Pageable pageable) {
        return userService.getReviewersBySubmissionId(submissionId, pageable);
    }

    @RequestMapping(value = "/submissions/{submissionId}/reviewers", method = RequestMethod.DELETE)
    public void deleteReviewers(@PathVariable("submissionId") Long submissionId, @RequestBody List<Long> reviewers) {
        submissionService.deleteReviewers(submissionId, reviewers);
    }

    @RequestMapping(value = "/submissions/{submissionId}/documents", method = RequestMethod.POST,
            consumes = {"multipart/form-data"})
    public SubmissionDto uploadDocument(@PathVariable("submissionId") Long submissionId, @RequestPart("file") MultipartFile file) throws IOException {
        return submissionService.uploadDocument(submissionId, file);
    }

    @RequestMapping(value = "/submissions/{submissionId}/documents", method = RequestMethod.GET)
    public List<DocumentDto> getDocuments(@PathVariable("submissionId") Long submissionId) {
        return submissionService.getDocuments(submissionId);
    }


}
