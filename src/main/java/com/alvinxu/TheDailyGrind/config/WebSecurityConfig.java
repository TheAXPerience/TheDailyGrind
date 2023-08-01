package com.alvinxu.TheDailyGrind.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.alvinxu.TheDailyGrind.security.LoginUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Bean
	public UserDetailsService userDetailsService() {
		return new LoginUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(authz -> authz
				.disable()
			).authorizeHttpRequests((authz) -> authz
				.requestMatchers("/login/**", "/register/**").permitAll()
				.requestMatchers("/**").authenticated()
			).formLogin(form  -> form
				.loginPage("/login")
				.defaultSuccessUrl("/home")
				.failureUrl("/login?error=true")
				.permitAll()
			).logout(form -> form
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout=true")
				.deleteCookies("JSESSIONID")
				.permitAll()
			).build();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public MessageSource messageSource() {
	  ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	  messageSource.setBasename("classpath:messages"); // find properties file
	  messageSource.setDefaultEncoding("UTF-8"); // string format
	  return messageSource;
	}
	
	@Bean
	LocalValidatorFactoryBean getValidator() {
	  LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	  bean.setValidationMessageSource(messageSource());
	  return bean;
	}
}
