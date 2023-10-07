package com.nikolaev.review;

import com.nikolaev.document.Document;
import com.nikolaev.review.status.ReviewStatus;
import com.nikolaev.user.User;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "submitted")
    private boolean submitted;

    @Column(name = "evaluation")
    private String evaluation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // Review - User(author)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    // Review - Submission
    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    // Submission - Status
    @ManyToOne
    @JoinColumn(name = "status_id")
    private ReviewStatus status;
}
