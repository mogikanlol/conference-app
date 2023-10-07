package com.nikolaev.submission_user_roles;


import com.nikolaev.submission.Submission;
import com.nikolaev.submission_role.SubmissionRole;
import com.nikolaev.user.User;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "submission_user_roles", uniqueConstraints = @UniqueConstraint(columnNames = {"submission_id", "user_id"}))
public class SubmissionUserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "submission_user_roles_id_generator")
    @SequenceGenerator(name = "submission_user_roles_id_generator", sequenceName = "submission_user_roles_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private SubmissionRole role;
}
