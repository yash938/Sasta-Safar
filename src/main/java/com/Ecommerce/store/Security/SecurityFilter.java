package com.Ecommerce.store.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.MalformedLinkException;
import java.io.IOException;

@Component

public class SecurityFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private JWTSecurityHelper jwtSecurityHelper;
    
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        logger.info("Header {}" , authorization);

        String username = null;
        String token = null;

        if(authorization != null && authorization.startsWith("Bearer")){

            token = authorization.substring(7);
        }else{
            logger.info("Invalid Header value !!");
        };

        try{
           username =  this.jwtSecurityHelper.getUsernameFromToken(token);
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
            logger.info("Illegal Argument while fetching the username");
        }catch (MalformedJwtException ex){
            logger.info("Jwt Token is expired");
            ex.printStackTrace();
        }catch (ExpiredJwtException ex){
            logger.info("Some changes is done in token !! Invalid token");
            ex.printStackTrace();
        }catch (Exception ex){

            ex.printStackTrace();
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //fetch user datail from username

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean b = this.jwtSecurityHelper.validateToken(token, userDetails);

            if(b){

                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }else {
                logger.info("Validation failed");
            }
        }
        filterChain.doFilter(request,response);
    }
}
