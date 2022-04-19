package com.pledge.app.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pledge.app.config.jwt.JwtTokenFilterConfigurer;
import com.pledge.app.config.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Entry points
        http.authorizeRequests()//
                .antMatchers("/actuator/**").hasAnyRole("DEVELOPER")
                .antMatchers(HttpMethod.POST, "/login").permitAll()//
                .antMatchers(HttpMethod.POST, "/facebook/login").permitAll()//
                .antMatchers(HttpMethod.POST, "/signup").permitAll()//
                .antMatchers(HttpMethod.POST, "/account/verify").permitAll()//
                .antMatchers(HttpMethod.POST, "/resend/verification-code/**").permitAll()//
//                .antMatchers(HttpMethod.GET, "/swagger-ui/**",
//                        "/v3/api-docs/**", "/doc/**").permitAll()
                // Disallow everything else..
                .anyRequest()
                .authenticated().and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e) ->
                {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode rootNode = mapper.createObjectNode();
                    rootNode.put("status", HttpStatus.UNAUTHORIZED.value());
                    rootNode.put("error", HttpStatus.UNAUTHORIZED.name());
                    rootNode.put("message", "Unauthorized");

                    String jsonString = mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(rootNode);
                    response.getWriter().write(jsonString);
                });
        //.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN)); //easy mode


        // If a user try to access a resource without having enough permissions
//        http.exceptionHandling().accessDeniedPage("/login");

        // Apply JWT
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

        // Optional, if you want to test the API from a browser
        // http.httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
