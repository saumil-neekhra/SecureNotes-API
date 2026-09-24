package in.cg.secure_notes_api.security;

import in.cg.secure_notes_api.entity.User;
import in.cg.secure_notes_api.repository.UserRepository;
import in.cg.secure_notes_api.service.JwtUtilityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public final JwtUtilityService jus;
    public final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtilityService jus, UserDetailsService userDetailsService) {
        this.jus = jus;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String username = jus.extractUsername(token);

        if(SecurityContextHolder.getContext().getAuthentication() != null){
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(!jus.verifyToken(token, userDetails)){
            filterChain.doFilter(request,response);
            return;
        }

        Authentication authentication =
                new UsernamePasswordAuthenticationToken
                        (userDetails,null,userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);




        //extract Autharisation header
        //see if its correct
        //verify it
        //set security context if not filled already
        // move to next filter
        // alwys move to nxt filter dofilter() intset of returing
    }
}
