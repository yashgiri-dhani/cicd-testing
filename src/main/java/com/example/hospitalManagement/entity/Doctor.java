package com.example.hospitalManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false, length = 100)
    private String name ;

    @Column( nullable = false , length = 100)
    private String specialization ;


    @Column(nullable = false, unique = true, length = 100)
    private String email ;

    @OneToOne
    @MapsId
    private User user ;

    @ManyToMany(mappedBy = "doctors")
    @JsonBackReference("department-doctors")
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments = new ArrayList<>();
}
