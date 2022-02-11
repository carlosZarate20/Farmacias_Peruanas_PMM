package com.farmaciasperuanas.pmmli.monitor.aspect.security;

import com.farmaciasperuanas.pmmli.monitor.dto.DataResponseDTO;
import com.farmaciasperuanas.pmmli.monitor.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        String url = httpServletRequest.getRequestURI();
        if (token == null && !(url.contains("signIn") || url.contains("saveUser")) ) {
//        throw new JwtTokenMissingException("Acceso denegado",HttpStatus.FORBIDDEN.value(),"Prohibido el acceso");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(403);
            httpServletResponse.setCharacterEncoding("UTF-8");
            DataResponseDTO<Map<String, Object>> response = new DataResponseDTO();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setMessage("Acceso denegado");
            Map<String, Object> errorAttributes = new HashMap<>();
            errorAttributes.put("error", "No se ingresó el token");
            response.setData(errorAttributes);
            PrintWriter output = httpServletResponse.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInString = objectMapper.writeValueAsString(response);
            output.print(jsonInString);
            output.flush();
            return;
//      GenericError error = new GenericError(HttpStatus.FORBIDDEN.value(), "Acceso denegado", httpServletRequest.getRequestURI());
//      throw new ContainerException(error.getStatus(),error.getMessage(),error);
        }
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (CustomException ex) {
            //this is very important, since it guarantees the user is not authenticated at all
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);


    }

}
