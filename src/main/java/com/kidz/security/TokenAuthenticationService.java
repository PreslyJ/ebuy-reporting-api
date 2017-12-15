package com.kidz.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TokenAuthenticationService {
  public static String SECRET = "ThisIsASecret";
  public static String TOKEN_PREFIX = "Bearer";
  public static String HEADER_STRING = "Authorization";

  
  public static Authentication getAuthentication(HttpServletRequest request) {
    
	String token = request.getHeader(HEADER_STRING);
	
	if (token != null) {
		// parse the token.
		String user = Jwts.parser()
		      .setSigningKey(SECRET)
		      .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
		      .getBody()
		      .getSubject();
		
		List<?> roles   = Jwts.parser()
		          .setSigningKey(SECRET)
		          .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				  .getBody()
				  .get("roles",List.class);
		  
		Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
		  
		if (null != roles) {
		  ArrayList<GrantedAuthority> authsList = new ArrayList<>(roles.size());
		  for (Object role : roles) {
		      authsList.add(new SimpleGrantedAuthority(role.toString().replace("authority=","").replace("{", "").replace("}","")));
		}
		authorities = Collections.unmodifiableList(authsList);
	  } else 
	    authorities = Collections.emptyList();
	  
	  
	  return user != null ?
	      new UsernamePasswordAuthenticationToken(user, null, authorities) :
	      null;
	}
	return null;
  }
}