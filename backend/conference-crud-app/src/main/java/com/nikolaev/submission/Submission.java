package com.nikolaev.submission;

import com.nikolaev.conference.Conference;
import com.nikolaev.document.Document;
import com.nikolaev.submission.status.SubmissionStatus;
import com.nikolaev.submission_user_roles.SubmissionUserRoles;
import com.nikolaev.user.User;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "submission")
public class Submission {

    @OneToMany(mappedBy = "submission")
    private List<SubmissionUserRoles> submissionUserRoles;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "submission_id_generator")
    @SequenceGenerator(name = "submission_id_generator", sequenceName = "submission_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "reviewable")
    private boolean reviewable;

    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;

    // Submission - Document
    @OneToMany(mappedBy = "submission")
    private List<Document> documents = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;


    // Submission - Status
    @ManyToOne
    @JoinColumn(name = "status_id")
    private SubmissionStatus status;
}
