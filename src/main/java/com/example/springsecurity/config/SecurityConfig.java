package com.example.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .csrf(csrf -> csrf
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // 使用 Cookie 存儲 CSRF token
                        // 其他 CSRF 配置...
                )
                .authorizeHttpRequests((authorize) -> authorize
                        // spring security 6 之後 antMatchers(), mvcMathcers(), regexMatchers() 皆 removed
                        // 因此改用 requestMatchers
                        .requestMatchers("/login", "/forgot-password").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(
//                        withDefaults()
                        //客製化的login page
                        formLogin -> formLogin
                                .loginPage("/login")
//                               這個也是在6被棄用的寫法
//                                .defaultSuccessURL("/index", true)
                                .permitAll()
//                                基礎使用
                                .successHandler(new SimpleUrlAuthenticationSuccessHandler("/index"))
//                               或者使用以下代碼根據用戶的角色將他們重定向到不同的頁面
//                                .successHandler(new AuthenticationSuccessHandler() {
//
//                                    @Override
//                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                                                        Authentication authentication) throws IOException, ServletException {
//
//                                        String targetUrl = "/index";
//
//                                        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
//                                            targetUrl = "/admin";
//                                        }
//
//                                        response.sendRedirect(targetUrl);
//                                    }
//                                })
                );
        // @formatter:on
        return http.build();
    }

    // 範例user, 初期開發時可用
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
    // 使用自定义的 UserDetailsService 和 PasswordEncoder 配置 AuthenticationManagerBuilder

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public MethodSecurityExpressionHandler expressionHandler() {
//        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
//        handler.setPermissionEvaluator(customPermissionEvaluator());
//        return handler;
//    }
//
//    @Bean
//    public PermissionEvaluator customPermissionEvaluator() {
//        return new CustomPermissionEvaluator();
//    }
}
