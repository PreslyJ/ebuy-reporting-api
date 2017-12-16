package com.kidz;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.kidz.security.JWTAuthenticationFilter;
import com.kidz.security.TokenAuthenticationService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	@Value("${config.security.headerstr:Authorization}")
	private String HEADER_STRING;
	
	@Value("${config.security.token.prefix:Bearer}")
	private String TOKEN_PREFIX;
	
	@Value("${config.security.key:ED9X8B78BA5E74B43194FD88E5EBE}")
	private String SECRET;
	
	@Value("${config.security.securityEnabled:1}")
	private int securityEnabled;
	
	
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	
    	TokenAuthenticationService.HEADER_STRING=HEADER_STRING;
    	TokenAuthenticationService.TOKEN_PREFIX=TOKEN_PREFIX;
    	TokenAuthenticationService.SECRET=SECRET;
    	
    	httpSecurity.
    		authorizeRequests()
    		.antMatchers(HttpMethod.GET, "/health").permitAll();
    	
    	
    	if(securityEnabled==1){
    	   	httpSecurity.authorizeRequests()
    		.antMatchers(HttpMethod.GET, "/R1").fullyAuthenticated()
    		.antMatchers(HttpMethod.GET, "/R2").fullyAuthenticated()
    		.antMatchers(HttpMethod.GET, "/R3").fullyAuthenticated();

    		
			httpSecurity.authorizeRequests().anyRequest().permitAll();
			httpSecurity.addFilterBefore(new JWTAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
		}else
			httpSecurity.authorizeRequests().anyRequest().permitAll();
    		
    	httpSecurity.cors().disable();
    	
    	httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    	
    }

   
    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/
    
}