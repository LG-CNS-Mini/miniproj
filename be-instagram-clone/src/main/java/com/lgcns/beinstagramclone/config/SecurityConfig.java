package com.lgcns.beinstagramclone.config;

import com.lgcns.beinstagramclone.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // JWT 기반 인증이라 CSRF, CORS, 폼 로그인 등은 비활성화해요.
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // 서버가 상태를 유지하지 않는 Stateless 방식으로 설정해요.
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 이제 모든 경로에 대한 접근 권한은 여기서만 관리해요!
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                new AntPathRequestMatcher("/swagger-ui/**"),
                                new AntPathRequestMatcher("/v3/api-docs/**"),
                                new AntPathRequestMatcher("/api/v2/inspire/user/signup"),
                                new AntPathRequestMatcher("/api/v2/inspire/user/signin"),
                                new AntPathRequestMatcher("/api/v2/inspire/forcast/getData"),
                                new AntPathRequestMatcher("/api/v2/inspire/ai/chat"),
                                new AntPathRequestMatcher("/api/v2/inspire/ai/java"),
                                new AntPathRequestMatcher("/api/v2/inspire/sse/subscribe"),
                                new AntPathRequestMatcher("/api/v2/inspire/sse/notifications"),
                                new AntPathRequestMatcher("/api/v1/post"),
                                new AntPathRequestMatcher("/images/**")
                        ).permitAll() // 이 경로들은 모두 허용!
                        .anyRequest().authenticated() // 그 외 모든 요청은 로그인한 사용자만!
                )
                // 네가 만든 JwtFilter를 UsernamePasswordAuthenticationFilter 전에 추가해서 먼저 실행되게 해요.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                

        return http.build();
    }
}