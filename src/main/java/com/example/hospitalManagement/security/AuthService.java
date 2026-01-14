package com.example.hospitalManagement.security;

import com.example.hospitalManagement.dto.LoginRequestDto;
import com.example.hospitalManagement.dto.LoginResponseDto;
import com.example.hospitalManagement.dto.SignupRequestDto;
import com.example.hospitalManagement.dto.SignupResponseDto;
import com.example.hospitalManagement.entity.Patient;
import com.example.hospitalManagement.entity.User;
import com.example.hospitalManagement.ropository.PatientRepository;
import com.example.hospitalManagement.ropository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository ;
    private final BCryptPasswordEncoder passwordEncoder ;
    private  final AuthenticationManager authenticationManager ;
    private final AuthUtil authUtil ;
    private  final PatientRepository patientRepository ;

    public SignupResponseDto signup(SignupRequestDto signupRequestDto){

        // check that user is already not present in the database
        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

        if(user != null){
            throw new RuntimeException("user is already registered");
        }


        try{

            // save the user
            User user1 = userRepository.save(User.builder()
                    .username(signupRequestDto.getUsername())
                    .pass(passwordEncoder.encode(signupRequestDto.getPassword()))
                    .roles(signupRequestDto.getRoles())
                    .build()
            );

            Patient patient = Patient.builder()
                    .name(signupRequestDto.getName())
                    .email(signupRequestDto.getUsername())
                    .user(user1)
                    .build();

            patientRepository.save(patient);

            return new SignupResponseDto(
                    user1.getId(),
                    user1.getUsername() ,
                    user1.getRoles()
            );

        }
        catch (Exception e){
            System.out.println("error = " + e);
            System.out.println("Error in the signup");
        }

         return null ;

    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){

        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(loginRequestDto.getUsername() , loginRequestDto.getPassword()));


        // CustomUserDetailsService user ko return karega
        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(user.getId() , token);

    }

}
