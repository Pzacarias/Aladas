package ar.com.ada.api.aladas.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.secure.spi.GrantedPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ar.com.ada.api.aladas.entities.Usuario;
import ar.com.ada.api.aladas.security.jwt.JWTTokenUtil;
import io.jsonwebtoken.Claims;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario u = usuarioService.buscarPorUsername(username);

        if (u != null) {
            return new User(u.getUsername(), u.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    public UserDetails loadUserByUsername(String username, String jwtToken) throws UsernameNotFoundException {

        Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        claims.forEach((key, value) -> {

            authorities.add(new SimpleGrantedAuthority("CLAIM_" + key + "_" + value));

        });

        // Este devuelve el usuario, SIN la password y sin buscarlo en la DB
        // confiamos que es el token porque lo validaremos despues
        // esto nos permite no ir a buscar a la db todo el tiempo
        // IGUALMENTE, buscar en la DB como mecanismo de proteccion no esta mal.
        return new User(username, "", authorities);

    }
}