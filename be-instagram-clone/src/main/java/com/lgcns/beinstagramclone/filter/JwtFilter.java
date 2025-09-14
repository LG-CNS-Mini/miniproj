package com.lgcns.beinstagramclone.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter implements Filter{
    
    @Value("${jwt.secret}")
    private String secret ; 

    private Key key ; 

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));     
    }

    @Override
    public void doFilter(
            ServletRequest  request, 
            ServletResponse response, 
            FilterChain chain)
    throws IOException, ServletException {
        
        System.out.println("[debug] >>> JwtFilter doFilter");
        HttpServletRequest  req = (HttpServletRequest)request ; 
        HttpServletResponse res = (HttpServletResponse)response ;
        
        String path   = req.getRequestURI() ; 
        System.out.println("[debug] >>> client path "+path); 
        String method = req.getMethod() ;
        System.out.println("[debug] >>> client method : "+method);  
        
        
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
            res.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            res.setHeader("Access-Control-Allow-Credentials", "true");

            chain.doFilter(request, response);
            return ;
        }

        if(isPath(path)) {
            System.out.println(">>>>> 인증/인가 없이 필터 통과");
            chain.doFilter(request, response) ;
            return ;
        }

        String authHeader = req.getHeader("Authorization");
        System.out.println(">>>>> Authorization : "+authHeader); 
        if( authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(">>>>> if not Authorization : "); 
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    
            return ;
        }

        String token = authHeader.substring(7); 
        System.out.println(">>>>> token : "+token); 

        try{
            System.out.println(">>>>> token validation "); 
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            
            System.out.println(">>>>> 검증성공 -> 컨트롤로 이동 "); 
            chain.doFilter(request, response);
        }catch(Exception e) {
            System.out.println(">>>>> 검증실패 ->  "); 
            e.printStackTrace();
            return ; 
        }

    }

    // 특정 endpoint 에 대해서는 인가없이 컨트롤러 이동이 가능하도록 
    public boolean isPath(String path) {
        return      path.startsWith("/swagger-ui")
                ||  path.startsWith("/v3/api-docs")
                ||  path.startsWith("/api/v2/inspire/user/signup")
                ||  path.startsWith("/api/v2/inspire/user/signin")
                ||  path.startsWith("/api/v2/inspire/forcast/getData")
                ||  path.startsWith("/api/v2/inspire/ai/chat")
                ||  path.startsWith("/api/v2/inspire/ai/java")
                ||  path.startsWith("/api/v2/inspire/sse/subscribe")
                ||  path.startsWith("/api/v2/inspire/sse/notifications")
                ||  path.startsWith("/api/v1/post")
                ||  path.startsWith("/images/")
                ;
                
    }
}
