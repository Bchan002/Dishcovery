package finalProject.dishcoveryServer.jwtAutheticationFilter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import finalProject.dishcoveryServer.service.authenticationService.UserInfoService;
import finalProject.dishcoveryServer.service.authenticationService.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtService jwtSvc;

    @Autowired
    private UserInfoService userDetailsSvc;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       // Retrieve the Authorization header
    String authHeader = request.getHeader("Authorization");
    System.out.println("Auth Header: " + authHeader); // Debug log
    
    String token = null;
    String username = null;
    
    // Check if the header starts with "Bearer "
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7); // Extract token
        System.out.println("Token extracted: " + token.substring(0, Math.min(token.length(), 20)) + "..."); // Show first 20 chars
        
        try {
            username = jwtSvc.extractUsername(token); // Extract username from token
            System.out.println("Username extracted: " + username);
        } catch (Exception e) {
            System.out.println("Error extracting username: " + e.getMessage());
            e.printStackTrace();
        }
    } else {
        System.out.println("No valid Authorization header found");
    }
    
    // If the token is valid and no authentication is set in the context
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        try {
            UserDetails userDetails = userDetailsSvc.loadUserByUsername(username);
            System.out.println("User details loaded for: " + userDetails.getUsername());
            
            // Validate token and set authentication
            boolean isValidToken = jwtSvc.validateToken(token, userDetails);
            System.out.println("Token validation result: " + isValidToken);
            
            if (isValidToken) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                System.out.println("User authorities: " + userDetails.getAuthorities());
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set in SecurityContext");
            }
        } catch (Exception e) {
            System.out.println("Error during authentication process: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Continue the filter chain
    filterChain.doFilter(request, response);
    }

    
}
