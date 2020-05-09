package com.auth.jwt.app.filter;

import com.auth.jwt.app.security.service.MiUserDetails;
import com.auth.jwt.app.security.service.MiUserDetailsService;
import com.auth.jwt.app.security.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFiltroToken extends OncePerRequestFilter {

    /* ~ Propiedades
    ==================================== */
    @Autowired
    private MiUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;


    /* ~ Metodos
    ==================================== */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extraemos el header Authorization: es donde se encuentra el token enviado por el usuario
        // Podemos poner esta parte en la clase de utilidad del token
        final String headerAuth = request.getHeader("Authorization");

        String token = null;
        String username = null;
        // Extraemos el token de la cabecera
        if(headerAuth != null && headerAuth.startsWith("Bearer ")){
            // Lo extraemos quitando el "Bearer " para solo tener el token
            token = headerAuth.substring(7);
            // Buscamos el username del usuario en el token
            username = jwtUtil.extraerUsername(token);
        }

        // Validamos los valores extraidos del token y el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // obtenemos el nombre del usuario de nuestra BD y poblamos el UserDetails
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validamos el token si aun esta vigente y si concuerda con el usuario de la BD
            if (jwtUtil.validarToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken userPassAuthToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Generamos los detalles de la autenticacion por token
                userPassAuthToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // y establecemos el tipo de seguridad
                SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
            } // fin de la validacion del token con los datos de la BD
        }
        filterChain.doFilter(request, response);
    } // fin del metodo de filtradp

} // fin de la clase
