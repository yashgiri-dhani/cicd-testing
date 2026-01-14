package com.example.hospitalManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false, unique = true, length = 50)
    private String policyNumber ;

    @Column(nullable = false, length = 100)
    private String provider ;

    @Column(nullable = false)
    private LocalDate validUntil ;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt ;

    @OneToOne(mappedBy = "insurance")
    @JsonBackReference("patient-insurance")
    private Patient patient ;
}
