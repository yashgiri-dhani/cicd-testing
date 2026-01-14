package com.example.hospitalManagement.ropository;

import com.example.hospitalManagement.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor , Long> {
    Doctor findByEmail(String email);
}
