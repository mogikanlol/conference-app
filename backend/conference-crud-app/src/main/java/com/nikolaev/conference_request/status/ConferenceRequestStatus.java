package com.nikolaev.conference_request.status;


import com.nikolaev.conference_request.ConferenceRequest;
import com.nikolaev.conference_request.comment.ConferenceRequestComment;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "conference_request_status")
public class ConferenceRequestStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private ConferenceRequestStatusName name;

    @OneToMany(mappedBy = "status")
    private List<ConferenceRequest> conferenceRequests;

    @OneToMany(mappedBy = "status")
    private List<ConferenceRequestComment> requestComments;

}
