package com.spring.angular.reddit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.resource.errorhandling.PolicyException;
import com.spring.angular.reddit.resource.errorhandling.RequestError;
import com.spring.angular.reddit.resource.errorhandling.RequestErrorResource;
import com.spring.angular.reddit.security.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider,
                                   UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {
        String jwt = getJwtFromRequest(httpServletRequest);

        if (StringUtils.hasText(jwt)) {
            try {
                jwtProvider.validateToken(jwt);
            } catch (ExpiredJwtException ex) {
                // Token is expired. We have to cut the process here.
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.setContentType("application/json");

                RequestErrorResource requestErrorResource =
                        getRequestErrorResource(RequestErrorTypes.EXPIRED_ACCESS_TOKEN);

                ObjectMapper objectMapper = new ObjectMapper();
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(requestErrorResource));
                return;
            } catch (Exception e) {
                // Token is not valid. We have to cut the process here.
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.setContentType("application/json");

                RequestErrorResource requestErrorResource =
                        getRequestErrorResource(RequestErrorTypes.INVALID_ACCESS_TOKEN);

                ObjectMapper objectMapper = new ObjectMapper();
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(requestErrorResource));
                return;
            }

            String username = jwtProvider.getUsernameFromJwt(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    private RequestErrorResource getRequestErrorResource(RequestErrorTypes requestErrorTypes) {
        final PolicyException serviceException = new PolicyException(requestErrorTypes, null);
        final RequestError requestError = new RequestError();
        requestError.setPolicyException(serviceException);
        return new RequestErrorResource(requestError);
    }
}
