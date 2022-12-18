package com.epam.esm.config;

import com.epam.esm.filter.JwtTokenFilter;
import com.epam.esm.filter.UserAccessFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenFilter jwtTokenFilter;
    private final UserAccessFilter userAccessFilter;

    @Autowired
    public SecurityConfig(JwtTokenFilter jwtTokenFilter, UserAccessFilter userAccessFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.userAccessFilter = userAccessFilter;
    }

    public SecurityConfig(boolean disableDefaults, JwtTokenFilter jwtTokenFilter, UserAccessFilter userAccessFilter) {
        super(disableDefaults);
        this.jwtTokenFilter = jwtTokenFilter;
        this.userAccessFilter = userAccessFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/*").hasAuthority("actuator:action")
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(userAccessFilter, JwtTokenFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
