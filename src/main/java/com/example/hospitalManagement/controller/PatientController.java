package com.example.hospitalManagement.controller;

import com.example.hospitalManagement.dto.AppointmentCreateRequest;
import com.example.hospitalManagement.dto.PatientDto;
import com.example.hospitalManagement.entity.Patient;
import com.example.hospitalManagement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService ;

    @PostMapping("/create/appointment/{id}")
    public void createNewAppointment(@PathVariable Long id , @RequestBody AppointmentCreateRequest appointmentCreateRequest){
          patientService.createNewAppointment(
                  id , appointmentCreateRequest
          );
    }

}
