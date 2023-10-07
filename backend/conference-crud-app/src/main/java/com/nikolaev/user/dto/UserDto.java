package com.nikolaev.user.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserDto implements Serializable {

    private  Long id;
    private  String username;
    private  String firstname;
    private  String lastname;
    private  String email;
    private  boolean enabled;

    public UserDto() {}

    public UserDto(Long id, String username, String firstname, String lastname, String email, boolean enabled) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
