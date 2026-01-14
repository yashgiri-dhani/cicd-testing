package com.example.hospitalManagement.service;

import com.example.hospitalManagement.entity.Insurance;
import com.example.hospitalManagement.entity.Patient;
import com.example.hospitalManagement.ropository.InsuranceRepository;
import com.example.hospitalManagement.ropository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository ;
    private final PatientRepository patientRepository ;

    @Transactional
    public Patient assignInsuranceToPatient(Insurance insurance , Long patientId){

        Patient patient = patientRepository.findById(patientId)
                  .orElseThrow(() -> new RuntimeException("Patient with specific id is not present " + patientId));

        patient.setInsurance(insurance);
        return patient ;
    }

    @Transactional
    public Patient detachInsuranceToPatient(Long patientId){

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient with specific id is not present " + patientId));

        patient.setInsurance(null);

        return  patient ;
    }
}
