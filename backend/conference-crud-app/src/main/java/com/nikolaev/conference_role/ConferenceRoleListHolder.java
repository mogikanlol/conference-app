package com.nikolaev.conference_role;

import com.nikolaev.conference_user_roles.ConferenceUserRoles;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ConferenceRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<ConferenceRole> roles) {
        this.roles = roles;
    }

    public List<ConferenceUserRoles> getConferenceUserRoles() {
        return conferenceUserRoles;
    }

    public void setConferenceUserRoles(List<ConferenceUserRoles> conferenceUserRoles) {
        this.conferenceUserRoles = conferenceUserRoles;
    }

}
