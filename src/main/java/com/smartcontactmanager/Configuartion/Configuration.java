package com.smartcontactmanager.Configuartion;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@org.springframework.context.annotation.Configuration
public class Configuration extends WebSecurityConfigurerAdapter{
	
			@Bean
			public UserDetailsService getUserDetailsService()
			{
				return new CustomUserDetailsServiceImpl();
			}
			
			@Bean
			public BCryptPasswordEncoder passwordEncoder()
			{
				return new BCryptPasswordEncoder();
			}
			
			@Bean
			public DaoAuthenticationProvider authenticationProvider()
			{
				DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
				daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
				daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
				return daoAuthenticationProvider;
			}
			
			///configure methods
			@Override
			protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
				authenticationManagerBuilder.authenticationProvider(authenticationProvider());
				
			}

			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/user/**").hasRole("USER")
				.antMatchers("/**").permitAll().and().formLogin().loginPage("/signin")
				.loginProcessingUrl("/signinaction")
				.defaultSuccessUrl("/user/dashboard")
				.and().csrf().disable();
			}
			
			
			
}
