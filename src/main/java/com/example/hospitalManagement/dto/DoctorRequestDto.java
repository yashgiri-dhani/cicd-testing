package com.example.hospitalManagement.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRequestDto {

    private String specialization;

      // because @OneToOne with User
}
