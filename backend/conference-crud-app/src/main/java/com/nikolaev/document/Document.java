package com.nikolaev.document;

import com.nikolaev.review.Review;
import com.nikolaev.submission.Submission;
import com.nikolaev.submission.status.SubmissionStatus;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_id_generator")
    @SequenceGenerator(name = "document_id_generator", sequenceName = "document_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "fileName")
    private String filename;

    @Lob
    @Column(name = "fileData", length = 100000)
    private byte[] data;

    // Submission - Status
    @ManyToOne
    @JoinColumn(name = "status_id")
    private SubmissionStatus status;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;

    // Submission - Review
    @OneToMany(mappedBy = "document")
    private List<Review> reviews;

    public Document() {
        this.reviews = new ArrayList<>();
    }
}
