package com.example.hospitalManagement.ropository;

import com.example.hospitalManagement.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance , Long> {
}
