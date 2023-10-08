package com.nikolaev.conference;


import com.nikolaev.conference_user_roles.ConferenceUserRoles;
import com.nikolaev.new_role_system.UserRoleInConf;
import com.nikolaev.submission.Submission;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "conference")
public class Conference implements Serializable {

    @OneToMany(mappedBy = "conference")
    private List<ConferenceUserRoles> conferenceUserRoles;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conference_id_generator")
    @SequenceGenerator(name = "conference_id_generator", sequenceName = "conference_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "acronym")
    private String acronym;
    @Column(name = "webPage")
    private String webPage;

    @Column(name = "expirationDate")
    private Date expirationDate;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;


    // Conference - Submission
    @OneToMany(mappedBy = "conference")
    private List<Submission> submissions = new ArrayList<>();

    @OneToMany(mappedBy = "conference")
    private List<UserRoleInConf> userRoleInConfList;
}
