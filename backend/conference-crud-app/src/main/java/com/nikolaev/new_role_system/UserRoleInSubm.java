package com.nikolaev.new_role_system;

import com.nikolaev.submission.Submission;
import com.nikolaev.submission_role.SubmissionRoleName;
import com.nikolaev.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserRoleInSubm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_in_subm_id_generator")
    @SequenceGenerator(name = "user_role_in_subm_id_generator", sequenceName = "user_role_in_subm_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "submission_id")
    private Long submissionId;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private SubmissionRoleName role;

    @ManyToOne
    @JoinColumn(name = "submission_id", insertable = false, updatable = false)
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
