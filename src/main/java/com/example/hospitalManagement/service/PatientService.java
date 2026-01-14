package com.example.hospitalManagement.service;

import com.example.hospitalManagement.dto.AppointmentCreateRequest;
import com.example.hospitalManagement.dto.PatientDto;
import com.example.hospitalManagement.entity.Appointment;
import com.example.hospitalManagement.entity.Doctor;
import com.example.hospitalManagement.entity.Patient;
import com.example.hospitalManagement.entity.User;
import com.example.hospitalManagement.ropository.AppointmentRepository;
import com.example.hospitalManagement.ropository.DoctorRepository;
import com.example.hospitalManagement.ropository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

@RequiredArgsConstructor

public class PatientService {

    private final PatientRepository patientRepository ;
    private final ModelMapper modelMapper ;
    private final DoctorRepository doctorRepository ;
    private final AppointmentRepository appointmentRepository ;

    public Patient createPatient(Patient patient){

        Patient newPatient = patientRepository.save(patient);
        return newPatient ;
    }

    public List<PatientDto> getAllPatient(){
        List<Patient> patients =  patientRepository.findAll();



        return patients.stream()
                .map(patient -> modelMapper.map(patient, PatientDto.class))
                .toList();
    }

    @Transactional
    public void createNewAppointment(Long doctorId , AppointmentCreateRequest appointmentCreateRequest){


        // extract the user_id(Patient_id) from the token
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long patientId = user.getId() ;

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor with specific id is not present " + doctorId));



        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient with specific id is not present " + patientId));

        // chane the dto into the entity

        Appointment appointment = modelMapper.map(appointmentCreateRequest , Appointment.class);

        if(appointment.getId() != null){
            throw new RuntimeException("Appointment is not new");
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        Appointment appointment1 =  appointmentRepository.save(appointment);



    }


}
