package com.example.hospitalManagement.service;

import com.example.hospitalManagement.dto.DoctorResponseDto;
import com.example.hospitalManagement.entity.Doctor;
import com.example.hospitalManagement.entity.User;
import com.example.hospitalManagement.entity.type.RoleType;
import com.example.hospitalManagement.ropository.DoctorRepository;
import com.example.hospitalManagement.ropository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository ;
    private final DoctorRepository doctorRepository ;
    private final ModelMapper modelMapper ;


    @Transactional
    public DoctorResponseDto assignDoctorRole(Long id, String specialization) {
        // check that given id is user or not
        User user = userRepository.findById(id).orElse(null);

        if(user == null){
            throw new RuntimeException("User is not found with this id = " + id);
        }


        if(user.getRoles().contains(RoleType.DOCTOR)){
            throw new RuntimeException("The User is already a doctor");
        }

        // give the role to the user
        user.getRoles().add(RoleType.DOCTOR);

        if (user.getPatient() == null) {
            throw new RuntimeException("Patient record not found for this user");
        }

        String name = user.getPatient().getName();
        if (!name.startsWith("Dr.")) {
            name = "Dr. " + name;
        }


        // save the doctor database
        Doctor doctor = Doctor.builder()
                .name(name)
                .email(user.getUsername())
                .specialization(specialization)
                .user(user)
                .build() ;

        Doctor doctor1 = doctorRepository.save(doctor);
        return modelMapper.map(doctor1 , DoctorResponseDto.class);
    }

    public void addDoctorInTheDepartment(Long id){
    }


}
