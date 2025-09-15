package com.lgcns.beinstagramclone.config;

import com.lgcns.beinstagramclone.filter.JwtFilter;
import com.lgcns.beinstagramclone.user.auth.OAuth2LoginSuccessHandler;
import com.lgcns.beinstagramclone.user.service.OAuth2DetailsService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
        
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // JWT 기반 인증이라 CSRF, CORS, 폼 로그인 등은 비활성화해요.
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        // ⭐ 로그아웃 성공 시 리다이렉트하지 않고, HTTP 상태 코드로 응답하도록 설정해요.
                        .logoutSuccessHandler((request, response, authentication) -> {
                                response.setStatus(HttpServletResponse.SC_OK);
                        })
                )

                // 서버가 상태를 유지하지 않는 Stateless 방식으로 설정해요.
                // JWT 토큰 기반 인증이므로 세션은 사용하지 않아요.
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ⭐ OAuth2 로그인 설정을 추가했어요! ⭐
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)                        
                        // 로그인 성공 시 리다이렉션 될 URL을 설정해요.
                        //.defaultSuccessUrl("/oauth2/redirect", true)
                        // 로그인 실패 시 리다이렉션 될 URL을 설정해요.
                        .failureUrl("/main")
                )
                

                // 이제 모든 경로에 대한 접근 권한은 여기서만 관리해요!
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                new AntPathRequestMatcher("/swagger-ui/**"),
                                new AntPathRequestMatcher("/v3/api-docs/**"),
                                new AntPathRequestMatcher("/api/v2/inspire/user/signup"),
                                new AntPathRequestMatcher("/api/v2/inspire/user/signin"),
                                new AntPathRequestMatcher("/auth/api/v2/inspire/user/logout"),
                                new AntPathRequestMatcher("/api/v2/inspire/forcast/getData"),
                                new AntPathRequestMatcher("/api/v2/inspire/ai/chat"),
                                new AntPathRequestMatcher("/api/v2/inspire/ai/java"),
                                new AntPathRequestMatcher("/api/v2/inspire/sse/subscribe"),
                                new AntPathRequestMatcher("/api/v2/inspire/sse/notifications"),
                                new AntPathRequestMatcher("/api/v1/post"),
                                new AntPathRequestMatcher("/images/**")
                        ).permitAll() // 이 경로들은 모두 허용!
                        // Preflight 요청 (OPTIONS)도 모두 허용하도록 추가해요.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청은 로그인한 사용자만!
                        
                )
                // 네가 만든 JwtFilter를 UsernamePasswordAuthenticationFilter 전에 추가해서 먼저 실행되게 해요.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                

        return http.build();
    }   

    // ⭐ CORS 설정을 위한 Bean을 추가했어요! ⭐
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(3600L);

        // ⭐ 프론트엔드에서 읽을 수 있도록 토큰 헤더를 노출시켜요!
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Refresh-Token"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}