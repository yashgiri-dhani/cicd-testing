package com.example.hospitalManagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false, unique = true, length = 100)
    private String name ;

    @OneToOne
    private Doctor headDoctor ;

    @ManyToMany
    @JoinTable(
            name = "my_dpt_doctor",
            joinColumns = @JoinColumn(name = "dpt_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    @JsonManagedReference("department-doctors")
    private Set<Doctor> doctors = new HashSet<>();
}
