package com.nikolaev.conference_user_roles;

import com.nikolaev.conference.Conference;
import com.nikolaev.conference_role.ConferenceRoleListHolder;
import com.nikolaev.user.User;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "conference_user_roles", uniqueConstraints = @UniqueConstraint(columnNames = {"conference_id", "user_id", "role_list_id"}))
public class ConferenceUserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conference_user_roles_id_generator")
    @SequenceGenerator(name = "conference_user_roles_id_generator", sequenceName = "conference_user_roles_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_list_id")
    private ConferenceRoleListHolder roleList;
}
