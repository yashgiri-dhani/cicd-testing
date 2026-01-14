package com.example.hospitalManagement.service;


import com.example.hospitalManagement.entity.Appointment;
import com.example.hospitalManagement.entity.Doctor;
import com.example.hospitalManagement.entity.Patient;
import com.example.hospitalManagement.ropository.AppointmentRepository;
import com.example.hospitalManagement.ropository.DoctorRepository;
import com.example.hospitalManagement.ropository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
@RequiredArgsConstructor
public class AppoinmentService {

    private final AppointmentRepository appointmentRepository ;
    private final DoctorRepository doctorRepository ;
    private final PatientRepository patientRepository ;


    @Transactional
    public void createNewAppointment(Appointment appointment , Long doctorId , Long patientId){

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor with specific id is not present " + doctorId));



        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient with specific id is not present " + patientId));

        if(appointment.getId() != null){
            throw  new RuntimeException("This is not the correct appointment");
        }

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        Appointment appointment1 =  appointmentRepository.save(appointment);


    }

    @Transactional
    public void reAssignAppointmentToAnotherDoctor(Long appointmentId , Long doctorId){

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();

        appointment.setDoctor(doctor);   // this will automatically update

        doctor.getAppointments().add(appointment);  // just Ufor by directional consistency


    }

}
