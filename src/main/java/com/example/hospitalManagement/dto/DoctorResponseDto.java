package com.example.hospitalManagement.dto;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DoctorResponseDto {

    private String name ;
    private String email ;
    private String specialization ;
}
