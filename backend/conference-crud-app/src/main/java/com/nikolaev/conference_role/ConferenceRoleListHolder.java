package com.nikolaev.conference_role;

import com.nikolaev.conference_user_roles.ConferenceUserRoles;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "role_list")
public class ConferenceRoleListHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_list_id_generator")
    @SequenceGenerator(name = "role_list_id_generator", sequenceName = "role_list_id_seq", allocationSize = 1)
    private Long id;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_list_roles",
            joinColumns = {@JoinColumn(name = "role_list_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<ConferenceRole> roles;

    @OneToMany(mappedBy = "roleList")
    private List<ConferenceUserRoles> conferenceUserRoles;

}

// role_list_roles
// role_list_id == role_list.id
// role_id  === conference_role.id

// list of conference roles