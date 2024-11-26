package com.Ecommerce.store.Config;


import com.Ecommerce.store.Security.AuthEntryPoint;
import com.Ecommerce.store.Security.JwtAuthenticationFilter;
import io.jsonwebtoken.security.Password;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.naming.AuthenticationException;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {



        httpSecurity.csrf(csrf -> csrf.disable())
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(List.of("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedMethods(List.of("*"));
                        configuration.setAllowedHeaders(List.of("*"));
                        configuration.setMaxAge(4000L);
                        return configuration;
                    }
                }))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/user/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/product/**").authenticated()
                                .requestMatchers("/product/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/product/**").hasRole("ADMIN")
                                .requestMatchers("/order/**").hasAnyRole("ADMIN","NORMAL")
                                .requestMatchers("/order/**").hasRole("ADMIN")
                                .requestMatchers("/category/create").hasRole("ADMIN")
                                .requestMatchers("/auth/generateToken").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()

                                .anyRequest().permitAll()


                );


//        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.exceptionHandling(ex ->ex.authenticationEntryPoint(authEntryPoint));
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
