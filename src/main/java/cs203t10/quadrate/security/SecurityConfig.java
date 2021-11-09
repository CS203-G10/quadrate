package cs203t10.quadrate.security;

import cs203t10.quadrate.security.jwt.JwtAuthEntryPoint;
import cs203t10.quadrate.security.jwt.JwtRequestFilter;
import cs203t10.quadrate.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("HEAD", "GET", "PUT", "POST",
                        "DELETE", "PATCH").allowedHeaders("*");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                http
                .cors()
                .and()
                .csrf().disable()
                .headers().frameOptions().deny()
                .and()
                .authorizeRequests()
                        .antMatchers("/api/authenticate").permitAll()
                        .antMatchers("/api/notification", "/api/notification/**").hasAnyRole("ADMIN","USER")
                        .antMatchers("/api/interval", "/api/interval/**").hasAnyRole("ADMIN","USER")
                        .antMatchers(HttpMethod.GET, "/api/location", "/api/location/**").hasAnyRole("ADMIN","USER")
                        .antMatchers("/api/location", "/api/location/**").hasAnyRole("ADMIN")
                        .antMatchers("/api/user", "/api/user/**").hasRole("ADMIN")
                        .antMatchers("/api/message", "/api/message/**").hasRole("ADMIN")
                        .anyRequest().authenticated().and()
                        .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).and().sessionManagement()
                        // stateless session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                // filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
