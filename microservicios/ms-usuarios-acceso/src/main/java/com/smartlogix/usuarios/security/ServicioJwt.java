package com.smartlogix.usuarios.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Genera y valida tokens JWT según lo indicado en el README de arquitectura.
 * El token incluye id de usuario y roles para autorización posterior.
 */
@Component
public class ServicioJwt {

    private final SecretKey claveSecreta;
    private final long minutosExpiracion;

    public ServicioJwt(
            @Value("${smartlogix.jwt.secret}") String secreto,
            @Value("${smartlogix.jwt.expiration-minutes}") long minutosExpiracion) {
        byte[] bytesClave = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytesClave.length < 32) {
            bytesClave = (secreto + "SmartLogixJwtPadding2026").getBytes(StandardCharsets.UTF_8);
        }
        this.claveSecreta = Keys.hmacShaKeyFor(bytesClave);
        this.minutosExpiracion = minutosExpiracion;
    }

    /** Crea un token JWT firmado con los datos del usuario autenticado. */
    public String generarToken(Long idUsuario, String correo, List<String> roles) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + minutosExpiracion * 60_000);
        return Jwts.builder()
                .subject(correo)
                .claim("idUsuario", idUsuario)
                .claim("roles", roles)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(claveSecreta)
                .compact();
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(claveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public LocalDateTime calcularFechaExpiracion() {
        return LocalDateTime.now().plusMinutes(minutosExpiracion);
    }

    public long getMinutosExpiracion() {
        return minutosExpiracion;
    }
}
