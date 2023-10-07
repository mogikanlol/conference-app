package com.nikolaev.conference_request.comment;

import com.nikolaev.conference_request.ConferenceRequest;
import com.nikolaev.conference_request.status.ConferenceRequestStatus;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "conference_request_comment")
public class ConferenceRequestComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    private Date date;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ConferenceRequest request;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private ConferenceRequestStatus status;

}
