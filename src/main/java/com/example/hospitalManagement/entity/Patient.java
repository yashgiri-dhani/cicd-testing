package com.example.hospitalManagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false)
    private String name ;


    private String email ;
    private String gender ;
    private LocalDate birthDate ;
    private String bloodGroup ;

    @OneToOne
    @MapsId
    private User user ;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_insurance_id")
    @JsonManagedReference("patient-insurance")
    private Insurance insurance;

    @OneToMany(mappedBy = "patient")
    @JsonManagedReference("patient-appointments")
    private List<Appointment> appointments ;
}
