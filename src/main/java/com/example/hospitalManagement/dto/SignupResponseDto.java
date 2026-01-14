package com.example.hospitalManagement.dto;


import com.example.hospitalManagement.entity.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SignupResponseDto {

    private Long id ;
    private String username ;
    Set<RoleType> roles ;
}
