package com.nikolaev.conference;

import com.nikolaev.submission.status.SubmissionStatusName;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ConferenceStatistic implements Serializable {
    Long submissionAmount;
    Long submissionAcceptedAmount;
    Long reviewAmount;
    Long documentAmount;
    Long participantAmount;

    public ConferenceStatistic(Conference conference) {
        this.submissionAmount = countSubmissionAmount(conference);
        this.submissionAcceptedAmount = countSubmissionAcceptedAmount(conference);
        this.reviewAmount = countReviewAmount(conference);
        this.documentAmount = countDocumentAmount(conference);
        this.participantAmount = countParticipantAmount(conference);
    }

    private Long countSubmissionAmount(Conference conference) {
        return (long) conference.getSubmissions().size();
    }

    private Long countSubmissionAcceptedAmount(Conference conferece) {
        return conferece.getSubmissions().stream()
                .filter(submission -> submission.getStatus().getName().equals(SubmissionStatusName.ACCEPT)).count();
    }

    private Long countReviewAmount(Conference conference) {
        return conference.getSubmissions().stream()
                .map(submission -> submission.getDocuments().stream().map(document -> document.getReviews()).mapToLong(value -> value.size()).sum())
                .mapToLong(Long::longValue).sum();

    }


    private Long countDocumentAmount(Conference conference) {
        return conference.getSubmissions().stream().map(submission -> submission.getDocuments().size()).mapToLong(Integer::longValue).sum();
    }

    private Long countParticipantAmount(Conference conference) {
        return conference.getUserRoleInConfList().stream()
                .map(r -> r.getUser())
                .distinct()
                .count();


        // return conference.getConferenceUserRoles().stream().map(conferenceUserRoles -> conferenceUserRoles.getUser()).count();
    }
}
