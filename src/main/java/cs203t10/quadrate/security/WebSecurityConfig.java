package cs203t10.quadrate.security;

import cs203t10.quadrate.security.jwt.AuthEntryPointJwt;
import cs203t10.quadrate.security.jwt.AuthTokenFilter;
import cs203t10.quadrate.user.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
//        securedEnabled = true,
//        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and() //  "and()"" method allows us to continue configuring the parent
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/user", "/users/**").permitAll() // Anyone can view books and reviews
//                .antMatchers(HttpMethod.POST, "/books").authenticated()
//                .antMatchers(HttpMethod.PUT, "/books/*").authenticated()
//                .antMatchers(HttpMethod.DELETE, "/books/*").authenticated()
//
//                // your code here
//                .antMatchers(HttpMethod.POST, "/books/*/reviews").hasAnyRole("ADMIN", "USER")
//                .antMatchers(HttpMethod.PUT, "/books/*/reviews/*").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/books/*/reviews/*").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
//                .antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")


                .and()
                .csrf().disable()
                .formLogin().disable()
                .headers().disable();

//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
