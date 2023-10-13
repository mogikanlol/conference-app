package com.nikolaev.submission;

import com.nikolaev.document.dto.DocumentDto;
import com.nikolaev.submission.dto.SubmissionDto;
import com.nikolaev.user.UserService;
import com.nikolaev.user.dto.BriefUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/submissions/")
@RequiredArgsConstructor
public class SubmissionResource {

    private final SubmissionService submissionService;
    private final UserService userService;

    /**
     * Returns submission by id
     *
     * @param submissionId Submission id
     * @return Submission
     */
    @GetMapping("/{submissionId}")
    public SubmissionDto getSubmission(@PathVariable("submissionId") Long submissionId) {
        log.info("in getSubmission()");
        return submissionService.getSubmission(submissionId);
    }

    /**
     * Returns all submission
     *
     * @return Submissions from specific conference
     */
    @GetMapping
    public List<SubmissionDto> getAll(@RequestParam(name = "reviewable", required = false) boolean reviewable) {
        return submissionService.getAll(reviewable);
    }


    @PutMapping("/{submissionId}/reviewable")
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
    @PostMapping("/{submissionId}/reviewers")
    public void addReviewers(@PathVariable("submissionId") Long submissionId, @RequestBody List<Long> reviewers) {
        submissionService.addReviewers(submissionId, reviewers);
    }

    @GetMapping("/{submissionId}/reviewers")
    public Page<BriefUserDto> getReviewers(@PathVariable("submissionId") Long submissionId, @PageableDefault Pageable pageable) {
        return userService.getReviewersBySubmissionId(submissionId, pageable);
    }

    @DeleteMapping("/{submissionId}/reviewers")
    public void deleteReviewers(@PathVariable("submissionId") Long submissionId, @RequestBody List<Long> reviewers) {
        submissionService.deleteReviewers(submissionId, reviewers);
    }

    @PostMapping(path = "/{submissionId}/documents", consumes = {"multipart/form-data"})
    public SubmissionDto uploadDocument(@PathVariable("submissionId") Long submissionId, @RequestPart("file") MultipartFile file) throws IOException {
        return submissionService.uploadDocument(submissionId, file);
    }

    @GetMapping("/{submissionId}/documents")
    public List<DocumentDto> getDocuments(@PathVariable("submissionId") Long submissionId) {
        return submissionService.getDocuments(submissionId);
    }


}
