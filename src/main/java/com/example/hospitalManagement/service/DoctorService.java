package com.example.hospitalManagement.service;


import com.example.hospitalManagement.dto.AppointmentResponseDto;
import com.example.hospitalManagement.dto.DoctorResponseDto;
import com.example.hospitalManagement.entity.Doctor;
import com.example.hospitalManagement.ropository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class DoctorService {

    private final DoctorRepository doctorRepository ;
    private final ModelMapper modelMapper ;

    @Transactional
    public Doctor createNewDoctor(Doctor doctor){

        // check that doctor is already not present
        Doctor existingDoctor = doctorRepository.findByEmail(doctor.getEmail());

        if(existingDoctor != null){
            throw new RuntimeException("Doctor with email : " + doctor.getEmail() + " is already present");

        }

        return doctorRepository.save(doctor);
    }

    public List<AppointmentResponseDto> getAllAppointmentOfDoctor(Long id){

        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor is not present with this id : " + id));

        return doctor.getAppointments()
                .stream()
                .map(a -> modelMapper.map(a, AppointmentResponseDto.class))
                .toList();

    }
}
