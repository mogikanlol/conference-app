package com.nikolaev.conference_request.comment;

import lombok.Getter;

import java.util.Date;

@Getter
public class ConferenceRequestCommentDto {
    private Long id;
    private Date date;
    private String content;
    private int status;

    public ConferenceRequestCommentDto() {
    }

    public ConferenceRequestCommentDto(Long id, Date date, String content, int status) {
        this.id = id;
        this.date = date;
        this.content = content;
        this.status = status;
    }
}
