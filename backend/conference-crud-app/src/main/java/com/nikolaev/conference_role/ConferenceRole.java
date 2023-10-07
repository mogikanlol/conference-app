package com.nikolaev.conference_role;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "conference_role")
public class ConferenceRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private ConferenceRoleName name;

    @ManyToMany(mappedBy = "roles")
    private List<ConferenceRoleListHolder> roleListHolder;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof ConferenceRole)) {
            return false;
        }
        ConferenceRole role = (ConferenceRole) obj;
        return role.getName().equals(name);
    }

}
