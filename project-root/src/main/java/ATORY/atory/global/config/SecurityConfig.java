package ATORY.atory.global.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // 필요 시 CSRF 해제
                .authorizeHttpRequests(auth -> auth
                        // Swagger 접근 허용
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // api 허용
                        .requestMatchers("/api/**"
                        ,"/api/gallery/register/**").permitAll()

                        // 그 외 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // 브라우저 로그인/리다이렉트 비활성화
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 인증 실패 시 → 401 반환
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                );

        return http.build();
    }
}
