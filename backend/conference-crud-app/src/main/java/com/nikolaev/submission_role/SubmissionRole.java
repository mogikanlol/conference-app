package com.nikolaev.submission_role;

import com.nikolaev.submission_user_roles.SubmissionUserRoles;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "submission_role")
public class SubmissionRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private SubmissionRoleName name;

    @OneToMany(mappedBy = "role")
    private List<SubmissionUserRoles> userRoles;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof SubmissionRole)) {
            return false;
        }
        SubmissionRole role = (SubmissionRole) obj;
        return role.getName().equals(name);
    }
}
