package com.example.hospitalManagement.controller;

import com.example.hospitalManagement.dto.ApiResponse;
import com.example.hospitalManagement.dto.AppointmentResponseDto;
import com.example.hospitalManagement.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {

    private  final DoctorService doctorService ;

    @GetMapping("/get/appointments/{id}")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAllAppointmentOfDoctor(@PathVariable  Long id){
        try {
            List<AppointmentResponseDto> appointmentList = doctorService.getAllAppointmentOfDoctor(id);


            ApiResponse<List<AppointmentResponseDto>> response = ApiResponse.<List<AppointmentResponseDto>>builder()
                    .success(true)
                    .message("All the appointment of the doctor is fetched successfully")
                    .data(appointmentList)
                    .build();

            return ResponseEntity.ok(response);
        }
        catch(RuntimeException ex){

            ApiResponse<List<AppointmentResponseDto>> errorResponse =
                    ApiResponse.<List<AppointmentResponseDto>>builder()
                            .success(false)
                            .message(ex.getMessage())
                            .data(null)
                            .build();

            return ResponseEntity.status(400).body(errorResponse);

        }
        catch (Exception ex) {

            ApiResponse<List<AppointmentResponseDto>> errorResponse =
                    ApiResponse.<List<AppointmentResponseDto>>builder()
                            .success(false)
                            .message("Internal Server Error")
                            .data(null)
                            .build();

            return ResponseEntity.status(500).body(errorResponse);
        }

    }
}
