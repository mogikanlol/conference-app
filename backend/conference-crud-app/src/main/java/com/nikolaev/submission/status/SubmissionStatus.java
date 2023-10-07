package com.nikolaev.submission.status;

import com.nikolaev.document.Document;
import com.nikolaev.submission.Submission;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "submission_status")
public class SubmissionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private SubmissionStatusName name;

    @OneToMany(mappedBy = "status")
    private List<Submission> submissions;

    @OneToMany(mappedBy = "status")
    private List<Document> documents;
}
