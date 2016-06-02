package com.ex.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component( "restAuthenticationEntryPoint" )
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	@Override
	public void commence( HttpServletRequest request, HttpServletResponse response, AuthenticationException authException ) throws IOException{
        // this is very important for a REST based API login. 
        // WWW-Authenticate header should be set as FormBased , else browser will show login dialog with realm
        response.setHeader("WWW-Authenticate", "FormBased");
        response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
		response.sendError( HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage() );
	}


}
