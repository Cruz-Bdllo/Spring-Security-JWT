package com.auth.jwt.app.security.utils;

import com.auth.jwt.app.security.service.MiUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Clase que permite analizar el token de la solicitud, obteniendo el contenido de sus partes
 * (header, payload, signature), ademas de poder crear la estructura del token y el tipo de signature.
 */
public class JwtUtil {
    @Value("${token.palabra.secreta}")
    private String secreto;

    /**
     * Este metodo solo extrae una parte del token [header.payload.signature] siendo los claims registrados
     * los claims (iss, sub, aude, exp)
     * @param token requiere el token para extraer la parte del claim
     * @return El conjunto de Claims
     */
    private Claims extraerContenidoClaims(String token){
        // parser: convierte a String, establece la clave para determinar si el JWT es valido dentro del header
        return Jwts.parser().setSigningKey(secreto).parseClaimsJws(token).getBody();
    } // fin del metodo

    /** PENDIENTE A USAR (revisar)
     * Utilizamos un generico que tenga que trajabar con la interfaz Claims
     * @param token token que se pretende extraer
     * @param resolvedorClaims sera de tipo String <b>extraerUsername</b>
     * @param token Cualquier tipo de objeto (en este caso String <code>extraerUsername</code>)
     * @return Depende de quien lo invoque Object.class
     */
    private Claims extraerPartesToken(String token, Claims resolvedorClaims){
        final Claims claims = extraerContenidoClaims(token);
        return resolvedorClaims;
    } // fin del metodo

    /**
     * Extrae del claim el nombre que contiene el subject
     * @param token token al que se le extraera el claim subject.
     * @return el username del token
     */
    private String extraerUsername(String token){
        return extraerContenidoClaims(token).getSubject();
    }

    /**
     * Extrae del claim el contenido de "exp" para identificar el tiempo de vencimiento del token
     * @param token token a extraer el tiempo de vencimiento
     * @return La fecha de vencimiento.
     */
    private Date extraerTiempoVencimiento(String token){
        return extraerContenidoClaims(token).getExpiration();
    }

    /**
     * Determina si se acepta procesar o no el token de acuerdo a la fecha y hora actual
     * @param token Del que se extrae la fecha de vencimiento
     * @return si ya vencio o no el token
     */
    private boolean isTokenExpiration(String token){
        return extraerTiempoVencimiento(token).before(new Date());
    }

    /**
     * Metodo que preparar la estructura del payload poblandolo con el contenido de los claims (sub, issu, exp, etc):
     * <b>builder:</b> Construye la estructura del payload.
     * <b>setSubject:</b> Establece el nombre del usuario al payload.
     * <b>setIssuedAt:</b> Establece el tiempo en que se crea.
     * <b>setExpiration:</b> Tiempo que va a durar el token.
     * <b>signWith:</b> Cifra la palabra secreta con el algoritmo que se especifica
     * @param payload Sera el payload que sera usado para construir los claims sobre el.
     * @param subject Establece el username del token
     * @return el String del payload del token
     */
    private String prepararEstructuraToken(Map<String, Object> payload, String subject){
        return Jwts.builder()
                .setClaims(payload)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *60 * 10))
                .signWith(SignatureAlgorithm.HS512, secreto)
                .compact();
    } // fin del metodo

    /**
     * Es el encargado de crear el token por medio del <code>UserDetails</code> dado como parametro
     * invoca al metodo que construye la estructura (payload)-
     * @param userDetails para obtener el username del usuario autenticado.
     * @return el token [header.payload.signature]
     */
    private String creatToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return prepararEstructuraToken(claims, userDetails.getUsername());
    }


    private boolean validarToken(String token, UserDetails userDetails){
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpiration(token));
    }

} // fin de la clase utilidad
