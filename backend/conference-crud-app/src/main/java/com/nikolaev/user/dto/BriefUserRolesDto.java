package com.nikolaev.user.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BriefUserRolesDto extends BriefUserDto {
    private List<Integer> roles;

    public BriefUserRolesDto() {
    }


    public BriefUserRolesDto(Long id, String username, String email, String firstname, String lastname, List<Integer> roles) {
        super(id, username, email, firstname, lastname);
        this.roles = roles;
    }

}
