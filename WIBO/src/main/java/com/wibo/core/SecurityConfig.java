package com.wibo.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wibo.core.service.MyAppUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private MyAppUserDetailsService myAppUserDetailsService;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/login", "/logout", "/css/**", "/fonts/**", "/img/**", "/js/**", "favicon.ico").permitAll()
                .antMatchers("/AdministracionCursos/**").hasAuthority("Administrador")
                .antMatchers("/example/table-export").hasAuthority("Administrador")
                .antMatchers("/example/table-row-select").hasAnyAuthority("Consultas", "Administrador")
                .anyRequest().authenticated()
            .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/example/table-basic")
                .permitAll()
            .and().logout()//logout configuration
        		.logoutUrl("/logout")
        		.deleteCookies("JSESSIONID")
        		.invalidateHttpSession(true) 
        		.logoutSuccessUrl("/login")
        		.permitAll();
    }
	
	//*
	@Autowired
   	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
              auth.userDetailsService(myAppUserDetailsService).passwordEncoder(passwordEncoder);
	}
	//*/

}