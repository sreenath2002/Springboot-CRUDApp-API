package com.example.Springcrudappproject.configuration;

import com.example.Springcrudappproject.service.CustomSuccesHandler;
import com.example.Springcrudappproject.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    CustomSuccesHandler customSuccesHandler;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Bean
    public static PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request->request.requestMatchers("/admin-page", "/showFormUpdate/**")
                        .hasAnyAuthority("ADMIN").requestMatchers("/user-page").hasAnyAuthority("USER").requestMatchers("/registration").permitAll()
                        .anyRequest().authenticated()
                        )
                .formLogin(form->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .successHandler(customSuccesHandler)
//                                .defaultSuccessUrl("/admin-page")
                                .permitAll())

                .logout(form->form.invalidateHttpSession(true).clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").deleteCookies("JSESSIONID","remember-me").permitAll());


        return http.build();
    }
    @Autowired
    public void configure(AuthenticationManagerBuilder auth)throws Exception{
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
