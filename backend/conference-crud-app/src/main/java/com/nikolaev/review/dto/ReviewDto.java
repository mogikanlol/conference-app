package com.nikolaev.review.dto;


import com.nikolaev.user.dto.BriefUserDto;
import lombok.Getter;

import java.util.Date;

@Getter
public class ReviewDto {
    private Long id;
    private String title;
    private boolean submitted;
    private String evaluation;
    private Date date;
    private int status;
    private BriefUserDto author;

    public ReviewDto() {}

    public ReviewDto(Long id, String title, boolean submitted, String evaluation, Date date, int status, BriefUserDto author) {
        this.id = id;
        this.title = title;
        this.submitted = submitted;
        this.evaluation = evaluation;
        this.date = date;
        this.status = status;
        this.author = author;
    }

}
