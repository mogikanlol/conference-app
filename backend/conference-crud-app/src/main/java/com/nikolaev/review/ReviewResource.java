package com.nikolaev.review;


import com.nikolaev.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewResource {

    private final ReviewService reviewService;

    @GetMapping("{reviewId}")
    public ReviewDto getReview(@PathVariable("reviewId") Long reviewId) {
        return this.reviewService.getReview(reviewId);
    }

    @PutMapping
    public ReviewDto updateReview(@RequestBody ReviewDto reviewDto) {
        return this.reviewService.update(reviewDto);
    }

    @PostMapping("{reviewId}/submit")
    public ReviewDto submitReview(@PathVariable("reviewId") Long reviewId) {
        return this.reviewService.submitReview(reviewId);
    }

    @DeleteMapping("{reviewId}")
    public void deleteReview(@PathVariable("reviewId") Long reviewId) {
        this.reviewService.deleteReview(reviewId);
    }
}
