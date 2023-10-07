package com.nikolaev.conference_request;

import com.nikolaev.conference_request.comment.ConferenceRequestComment;
import com.nikolaev.conference_request.status.ConferenceRequestStatus;
import com.nikolaev.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "conference_request")
public class ConferenceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "acronym")
    private String acronym;
    @Column(name = "webPage")
    private String webPage;

    @Column(name = "expirationDate")
    private Date expirationDate;

    // ConferenceRequest - User(organizer)
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;


    @OneToMany(mappedBy = "request")
    private List<ConferenceRequestComment> comments;

    //ConferenceRequest - Status
    @ManyToOne
    @JoinColumn(name = "status_id")
    private ConferenceRequestStatus status;

}
