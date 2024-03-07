package com.ssafy.gallery.config;

import com.ssafy.gallery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CorsConfig corsConfig;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**", "/api-docs", "/api-docs/**", "/swagger-ui.html",
            "/api/v1/user/**", "/api/v1/user/login/**", "/index.html"
    };

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilter(corsConfig.corsFilter())
//                .addFilter(new JwtAuthenticationFilter(authenticationManager)) // AuthenticationManger
//                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
//                        .requestMatchers("/index.html", "/feed/**", "/albums/**", "/photo/**", "/user/signup", "/", "/login", "/album/init").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );  // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
        return http.build();
    }
}