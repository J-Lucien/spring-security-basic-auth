package example.hello_security.config;

import example.hello_security.filter.CustomAuthenticationSuccessHandler;
import example.hello_security.filter.CustomLogOutSuccessHandler;
import example.hello_security.filter.exception.CustomAccessDeniedHandler;
import example.hello_security.filter.exception.CustomAuthenticationEntryPoint;
import example.hello_security.filter.exception.CustomAuthenticationFailureHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class DefaultSecurityConfig {


//    @Bean
//    @ConditionalOnMissingBean(UserDetailsService.class)
//    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        return new InMemoryUserDetailsManager(User.withUsername("User")
//                .password("{noop}password")
//                .build());
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public CustomAuthenticationFailureHandler failureHandler(){
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public CustomAuthenticationSuccessHandler successHandler(){
        return new CustomAuthenticationSuccessHandler();
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
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
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
        return httpSecurity
                .csrf(Customizer.withDefaults())
                .formLogin(form -> form
                                .loginProcessingUrl("/login")
                                .loginPage("/login")
                        .failureHandler(failureHandler())
//                        .defaultSuccessUrl("/home") // ou
                        .successHandler(successHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                                .logoutSuccessUrl("/login?logout=true") // ceci est par defaut ou
//                        .logoutSuccessHandler(logOutSuccessHandler())
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**","/js/**","/images/**","/logout-success/**","/login/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedHandler(accessDeniedHandler()) // ou
//                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                )

                .build();
    }


}
