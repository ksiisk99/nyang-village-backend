package com.ay.talk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ay.talk.jwt.JwtAuthenticationEntryPoint;
import com.ay.talk.jwt.JwtAuthenticationFilter;
import com.ay.talk.jwt.JwtTokenProvider;

//import com.ay.talk.jwt.JwtAuthenticationEntryPoint;
//import com.ay.talk.jwt.JwtAuthenticationFilter;
//import com.ay.talk.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtEntryPoint;

	@Autowired
	public SecurityConfig(JwtTokenProvider jwtTokenProvider, JwtAuthenticationEntryPoint jwtEntryPoint) {
		// TODO Auto-generated constructor stub
		this.jwtTokenProvider=jwtTokenProvider;
		this.jwtEntryPoint=jwtEntryPoint;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		 web.ignoring().antMatchers("/v2/api-docs",
                 "/configuration/ui",
                 "/swagger-resources/**",
                 "/configuration/security",
                 "/swagger-ui.html",
                 "/webjars/**","/ay/login","/ay/pc/login","/ay/login2"
                 ,"/favicon.ico","/","/stomp","/ay/manage","/ay/manage/report");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub

		http
			.cors().and()
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().authorizeHttpRequests()
			.anyRequest().authenticated()
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(jwtEntryPoint) //여기서 인증에 관한 에러처리를 한다.
			.and()
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
					UsernamePasswordAuthenticationFilter.class);
	}
	
}
