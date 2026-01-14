package com.example.hospitalManagement.security;

import com.example.hospitalManagement.entity.User;
import com.example.hospitalManagement.ropository.UserRepository;
import com.example.hospitalManagement.security.AuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        try {

            log.info("incoming request : {}", request.getServletPath());


            String path = request.getServletPath();

            System.out.println("incoming path : " + path);

            if (path.equals("/user/signup") || path.equals("/user/login")) {
                filterChain.doFilter(request, response);
                return;
            }


            System.out.println("How are you");


            final String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Safe token extraction
            String token = header.substring(7);

            String username = authUtil.getUsernameFromToken(token);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userRepository.findByUsername(username).orElseThrow();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        }
        catch(Exception ex){
            response.setStatus(400);
            response.setContentType("application/json");

            String json = """
                    {
                      "success": false,
                      "message": "%s",
                      "data": null
                    }
                    """.formatted(ex.getMessage());

            response.getWriter().write(json);
        }
    }
}
