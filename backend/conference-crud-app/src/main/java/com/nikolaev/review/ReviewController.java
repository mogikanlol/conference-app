package com.nikolaev.review;


import com.nikolaev.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @RequestMapping(value = "{reviewId}", method = RequestMethod.GET)
    public ReviewDto getReview(@PathVariable("reviewId") Long reviewId) {
        return this.reviewService.getReview(reviewId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ReviewDto updateReview(@RequestBody ReviewDto reviewDto) {
        return this.reviewService.update(reviewDto);
    }

    @RequestMapping(value = "{reviewId}/submit", method = RequestMethod.POST)
    public ReviewDto submitReview(@PathVariable("reviewId") Long reviewId) {
        return this.reviewService.submitReview(reviewId);
    }

    @RequestMapping(value = "{reviewId}", method = RequestMethod.DELETE)
    public void deleteReview(@PathVariable("reviewId") Long reviewId) {
        this.reviewService.deleteReview(reviewId);
    }
}
