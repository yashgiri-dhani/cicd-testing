package com.example.hospitalManagement.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class PatientDto {
    private Long id;
    private String name;
    private String email;
    private String gender;
    private LocalDate birthDate;
    private String bloodGroup;
    private LocalDateTime createdAt;

    private InsuranceDto insurance;
    private List<AppointmentDto> appointments;
}
