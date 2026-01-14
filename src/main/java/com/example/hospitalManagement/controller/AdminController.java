package com.example.hospitalManagement.controller;

import com.example.hospitalManagement.dto.ApiResponse;
import com.example.hospitalManagement.dto.DoctorRequestDto;
import com.example.hospitalManagement.dto.DoctorResponseDto;
import com.example.hospitalManagement.dto.PatientDto;
import com.example.hospitalManagement.service.AdminService;
import com.example.hospitalManagement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService ;
    private final PatientService patientService ;

    @PostMapping("/assign/doctor/{id}")
    public ResponseEntity<ApiResponse<DoctorResponseDto>> assignDoctor(@PathVariable Long id ,
                                                                       @RequestHeader("specialization") String specialization){
          DoctorResponseDto doctor =  adminService.assignDoctorRole(id , specialization);

          ApiResponse<DoctorResponseDto> response = ApiResponse.<DoctorResponseDto>builder()
                .success(true)
                .message("Doctor role assigned successfully!")
                .data(doctor)
                .build();

          return ResponseEntity.ok(response);
    }


    @GetMapping("/getAllPatient")
    public List<PatientDto> getAllPatient(){
        return patientService.getAllPatient();
    }
}
