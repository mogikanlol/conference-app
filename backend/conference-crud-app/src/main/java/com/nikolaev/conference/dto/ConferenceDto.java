package com.nikolaev.conference.dto;

import com.nikolaev.submission.dto.BriefSubmissionDto;
import com.nikolaev.user.dto.BriefUserDto;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class ConferenceDto {
    private Long id;
    private String title;
    private String acronym;
    private String webPage;
    private Date expirationDate;
    private String city;
    private String country;
    private List<BriefSubmissionDto> submissions;
    private List<BriefUserDto> reviewers;
    private BriefUserDto organizer;

    public ConferenceDto() {
    }

    public ConferenceDto(Long id, String title, String acronym, String webPage, Date expirationDate, String city, String country, List<BriefSubmissionDto> submissions, List<BriefUserDto> reviewers, BriefUserDto organizer) {
        this.id = id;
        this.title = title;
        this.acronym = acronym;
        this.webPage = webPage;
        this.expirationDate = expirationDate;
        this.city = city;
        this.country = country;
        this.submissions = submissions;
        this.reviewers = reviewers;
        this.organizer = organizer;
    }
}
