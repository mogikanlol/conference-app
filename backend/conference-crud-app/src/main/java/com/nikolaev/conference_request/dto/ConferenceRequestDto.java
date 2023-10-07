package com.nikolaev.conference_request.dto;


import com.nikolaev.conference_request.comment.ConferenceRequestCommentDto;
import com.nikolaev.user.dto.BriefUserDto;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
public class ConferenceRequestDto implements Serializable {

    private Long id;
    private String title;
    private String acronym;
    private String webPage;
    private BriefUserDto organizer;
    private String city;
    private String country;
    private List<ConferenceRequestCommentDto> comments;
    private int status;
    private Date expirationDate;

    public ConferenceRequestDto() {}

    public ConferenceRequestDto(Long id, String title, String acronym, String webPage, BriefUserDto organizer, String city, String country, List<ConferenceRequestCommentDto> comments, int status, Date expirationDate) {
        this.id = id;
        this.title = title;
        this.acronym = acronym;
        this.webPage = webPage;
        this.organizer = organizer;
        this.city = city;
        this.country = country;
        this.comments = comments;
        this.status = status;
        this.expirationDate = expirationDate;
    }
}
