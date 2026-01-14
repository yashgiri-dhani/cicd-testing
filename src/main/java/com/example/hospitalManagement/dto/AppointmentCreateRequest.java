package com.example.hospitalManagement.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentCreateRequest {

    private LocalDateTime appointmentTime;

    private String reason;
}
