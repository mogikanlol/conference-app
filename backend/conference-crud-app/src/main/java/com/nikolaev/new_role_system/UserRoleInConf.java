package com.nikolaev.new_role_system;

import com.nikolaev.conference.Conference;
import com.nikolaev.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserRoleInConf {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_in_conf_id_generator")
    @SequenceGenerator(name = "user_role_in_conf_id_generator", sequenceName = "user_role_in_conf_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "conference_id")
    private Long conferenceId;

    @Column
    private Integer role;

    @ManyToOne
    @JoinColumn(name = "conference_id", insertable = false, updatable = false)
    private Conference conference;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
