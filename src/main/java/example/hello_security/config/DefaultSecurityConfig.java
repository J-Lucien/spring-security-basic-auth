package example.hello_security.config;

import example.hello_security.filter.*;
import example.hello_security.filter.exception.CustomAccessDeniedHandler;
import example.hello_security.filter.exception.CustomAuthenticationEntryPoint;
import example.hello_security.filter.exception.CustomAuthenticationFailureHandler;
import example.hello_security.provider.CustomDaoAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class DefaultSecurityConfig {

    private final IpBlockFilter ipBlockFilter;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CaptchaAuthenticationFilter captchaAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CustomLogOutSuccessHandler logOutSuccessHandler(){
        return new CustomLogOutSuccessHandler();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails userDetails = User.withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }



    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        CustomDaoAuthenticationProvider customDaoAuthenticationProvider = new CustomDaoAuthenticationProvider(passwordEncoder());
        customDaoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return new ProviderManager(customDaoAuthenticationProvider);
    }



    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher.class)
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }


//    FilterChain Configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        System.out.println("FilterChain Configuration");
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName("continue");
        return httpSecurity
                .csrf(Customizer.withDefaults())
                .formLogin(form -> form
                                .loginProcessingUrl("/login")
                                .loginPage("/login")
                        .failureHandler(failureHandler)
//                        .defaultSuccessUrl("/home") // ou
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                                .logoutSuccessUrl("/login?logout=true")
//                        .logoutSuccessHandler(logOutSuccessHandler())
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**","/js/**","/images/**","/logout-success/**","/login/**","/blocked/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .requestCache((cache) -> cache
                        .requestCache(requestCache)
                )
                .addFilterBefore(captchaAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(ipBlockFilter, CaptchaAuthenticationFilter.class)
                .build();
    }


}
