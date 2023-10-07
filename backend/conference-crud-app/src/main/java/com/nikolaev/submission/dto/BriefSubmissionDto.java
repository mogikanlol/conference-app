package com.nikolaev.submission.dto;

import com.nikolaev.user.dto.BriefUserDto;
import lombok.Getter;

import java.util.List;

@Getter
public class BriefSubmissionDto {
    private Long id;
    private String title;
    private BriefUserDto author;
    private int status;
    private List<BriefUserDto> reviewers;

    public BriefSubmissionDto() {
    }

    public BriefSubmissionDto(Long id, String title, BriefUserDto author, int status, List<BriefUserDto> reviewers) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.reviewers = reviewers;
    }

}
