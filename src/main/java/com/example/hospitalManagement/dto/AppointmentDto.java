package com.example.hospitalManagement.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentDto {
    private Long id;
    private LocalDate date;
}
