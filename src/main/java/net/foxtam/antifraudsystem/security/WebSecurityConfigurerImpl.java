package net.foxtam.antifraudsystem.security;

import net.foxtam.antifraudsystem.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPoint restAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfigurerImpl(AuthenticationEntryPoint restAuthenticationEntryPoint,
                                     UserDetailsService userDetailsService) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
                .and()
                .csrf().disable() // for Postman
                .authorizeRequests() // manage access
                
                .antMatchers(HttpMethod.POST, "/api/auth/user")
                .permitAll()
                
                .antMatchers("/actuator/shutdown")
                .permitAll()
                
                .mvcMatchers(HttpMethod.DELETE, "/api/auth/user")
                .hasRole(Role.ADMINISTRATOR.name())
                
                .mvcMatchers(HttpMethod.GET, "/api/auth/list")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.SUPPORT.name())
                
                .mvcMatchers(HttpMethod.POST, "/api/antifraud/transaction")
                .hasRole(Role.MERCHANT.name())
                
                .mvcMatchers(HttpMethod.PUT, "/api/auth/access", "/api/auth/role")
                .hasRole(Role.ADMINISTRATOR.name())
                
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
