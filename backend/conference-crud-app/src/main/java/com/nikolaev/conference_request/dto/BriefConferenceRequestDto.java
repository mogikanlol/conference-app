package com.nikolaev.conference_request.dto;

import com.nikolaev.user.dto.BriefUserDto;
import lombok.Getter;

@Getter
public class BriefConferenceRequestDto {
    private Long id;
    private String title;
    private BriefUserDto organizer;
    private int status;


    public BriefConferenceRequestDto() {}

    public BriefConferenceRequestDto(Long id, String title, BriefUserDto organizer, int status) {
        this.id = id;
        this.title = title;
        this.organizer = organizer;
        this.status = status;
    }
}
