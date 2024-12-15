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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizaion = request.getHeader("Authorization");

        logger.info("Header {}",authorizaion);

        String username = null;
        String token = null;

        if(authorizaion != null && authorizaion.startsWith("Bearer")){
            token = authorizaion.substring(7);

                try {
                    username = jwtHelper.getUsernameFromToken(token);
                    logger.info("Token username ",username);

                }catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    logger.info("illegal argument while fetching the argument " + e.getMessage());
                }
                catch (ExpiredJwtException ex){
                    logger.info("Given JWT is Expired " + ex.getMessage());
                }
                catch (MalformedJwtException ex){
                    logger.info("Some changed has done in token "+ex.getMessage());
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }


            }else{
                logger.info("Invalid Header");
            }


        //username null nahi hai toh
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //valid token
            if(username.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)){
                //security conte    ct ke andar authentication set karenge

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        };
        filterChain.doFilter(request,response);
    }
}
