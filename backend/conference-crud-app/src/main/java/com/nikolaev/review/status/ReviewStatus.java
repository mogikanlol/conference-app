package com.nikolaev.review.status;

import com.nikolaev.review.Review;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "review_status")
public class ReviewStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private ReviewStatusName name;

    @OneToMany(mappedBy = "status")
    private List<Review> reviews;

}
