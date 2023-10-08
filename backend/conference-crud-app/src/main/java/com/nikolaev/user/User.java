package com.nikolaev.user;

import com.nikolaev.conference_request.ConferenceRequest;
import com.nikolaev.conference_user_roles.ConferenceUserRoles;
import com.nikolaev.new_role_system.UserRoleInConf;
import com.nikolaev.new_role_system.UserRoleInSubm;
import com.nikolaev.review.Review;
import com.nikolaev.submission.Submission;
import com.nikolaev.submission_user_roles.SubmissionUserRoles;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {


    @OneToMany(mappedBy = "user")
    private List<ConferenceUserRoles> conferenceUserRoles;

    @OneToMany(mappedBy = "user")
    private List<SubmissionUserRoles> submissionUserRoles;

    @OneToMany(mappedBy = "author")
    private List<Submission> submissions;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_generator")
    @SequenceGenerator(name = "users_id_generator", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username")
//    @NotNull(message = "Username cannot be null")
//    @Size(min = 3, max = 15, message = "Username length must be between 3 and 15")
//    @Pattern(message = "Username not valid", regexp = "^[a-z0-9_-]+$")
    private String username;

    @Column(name = "password")
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password length must be more than 6")
    @Pattern.List({
            @Pattern(regexp = "(?=.*\\d).+", message = "Password must contain at least one digit"),
            @Pattern(regexp = "(?=.*[a-z]).+", message = "Password must contain at least one lowercase character"),
            @Pattern(regexp = "(?=.*[A-Z]).+", message = "Password must contain at least one uppercase character"),
            @Pattern(regexp = "(?=.*[@#$%]).+", message = "Password must contain at least one special character from the list @#$%"),
            @Pattern(regexp = "(?=\\S+$).+", message = "Password must contain no whitespace")

    })
    private String password;

    @Column(name = "firstname")
    @NotNull(message = "Firstname cannot be null")
    @NotBlank(message = "Firstname cannot be blank")
    private String firstname;

    @Column(name = "lastname")
    @NotNull(message = "Lastname cannot be null")
    @NotBlank(message = "Lastname cannot be blank")
    private String lastname;

    @Column(name = "email")
    @NotNull(message = "Email cannot be null")
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email not valid")
    private String email;

    @Column(name = "confirmed")
    private boolean confirmed;

    @Column(name = "confirmationToken")
    private String confirmationToken;

    private boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

//    @ManyToMany(mappedBy = "userRoleMap")
//    private List<Submission> submissions;

    // User - Authority
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    private List<Authority> authorities;

    // User - Request
    @OneToMany(mappedBy = "organizer")
    private List<ConferenceRequest> requests;

    // User(reviewer) - Review
    @OneToMany(mappedBy = "author")
    private List<Review> reviews;

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User) obj;
        return user.getUsername().equals(username);
    }

    @OneToMany(mappedBy = "user")
    private List<UserRoleInConf> userRoleInConfList;

    @OneToMany(mappedBy = "user")
    private List<UserRoleInSubm> test;
}

