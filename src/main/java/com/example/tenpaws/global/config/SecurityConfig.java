package com.example.tenpaws.global.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // rest api, stateless 방식 사용하므로 비활성화
        httpSecurity
                .formLogin(AbstractHttpConfigurer::disable);
        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable);

        // CORS 설정
        httpSecurity
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);

                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        }));

        // stateless 상태 활성화
        httpSecurity
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 모든 기능 완성되면 그 때 엔드포인트 보고 접근 권한 수정!
        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/join").permitAll()
                        .anyRequest().permitAll());


        return httpSecurity.build();
    }
}
