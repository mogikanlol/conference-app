package com.nikolaev.conference.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class BriefConferenceDto {

    private Long id;
    private String title;
    private Date expirationDate;

    public BriefConferenceDto() {}

    public BriefConferenceDto(Long id, String title, Date expirationDate) {
        this.id = id;
        this.title = title;
        this.expirationDate = expirationDate;
    }
}
