package com.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity // 1-1. 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebConfig {

    // 1-2. WebSecurityConfigurerAdapter를 상속해서 AuthenticationManager를 bean으로 등록했던걸 직접 등록.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
    }

    // 1-3. 기존 SecurityConfig에서 configure 메소드 기능을 한다.
    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
                .httpBasic().disable() // 1-4. httpSecurity가 제공하는 기본인증 기능 disable
                    .csrf().disable() // 1-5. CSRF[Cross Site Request Forgery] 토큰 비활성화
                    .cors() // 1-6. cors[Cross Origin Resource Sharing] 허용 : 서로 다른 Origin끼리 요청을 주고 받을 수 있게 허용.
                .and()
                    .headers().frameOptions().disable() // 1-7. spring-security에서는 자체적으로 X-Frame-Options를 deny해놓기 때문에 이것을 disable 해놓는다.
                .and()
                .authorizeRequests() // 1-8. 인증이 필요한 Request 정보
                    .antMatchers("/hello/**", "/user/**").permitAll()
        //authenticated() // 1-9. 해당 url은 인증이 필요하다.
          //          .anyRequest().permitAll() // 1-10. 1-9가 아닌 url은 모두 허용한다.
               .and()
                    .formLogin() // 1-13.
                    .loginPage("/login-form") // 1-14. 사용자가 1-9의 URL로 요청하면(GET 방식) /auth/signin으로 리다이렉트.
                    .loginProcessingUrl("/login") // 1-15. URL로 요청(POST 방식) -> 스프링 시큐리티가 구현한 UserDetailsService가 낚아채서 로그인 프로세스 진행
                    //.successHandler(new LoginSuccessHandler()) // 1-9. 1-8이 정상적으로 처리가 되었으면 LoginSuccessHandler에서 처리하게 위임.
                .and()
                .build();
    }

    // 1-12. 정적 파일 인증 무시.(2.7.0이상 부터는 이런 방식으로 설정. -> 임시로 bean으로 등록은 안해놓음)
    public WebSecurityCustomizer webSecurityCustomizer() {

        return new WebSecurityCustomizer() {

            @Override
            public void customize(WebSecurity web1) {
                web1.ignoring().antMatchers("/hello/**", "/user/**");
            }
        };
    }

    // 1-11. 비밀빈호 해시
    @Bean
    public BCryptPasswordEncoder encode() {
    return new BCryptPasswordEncoder();
    }
}
