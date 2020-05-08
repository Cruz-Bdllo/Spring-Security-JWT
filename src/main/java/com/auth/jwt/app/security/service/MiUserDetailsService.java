package com.auth.jwt.app.security.service;

import com.auth.jwt.app.entity.Usuario;
import com.auth.jwt.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Permite implementar el metodo para cargar los datos de un usuario especifico atraves de una BD.
 */
@Service
public class MiUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Cargamos los datos obtenidos de la consulta hacia la BD y retornamos un objeto <b>UserDetails</b>
     * como nuestra clase <b>MiUserDetails</b> lo implementa la podemos usar, en su constructor le pasamos el
     * usuario de la BD para poblarlo.
     * @param username nombre del usuario a buscar
     * @return UserDetails que poblara por medio de <b>Usuario</b>
     * @throws UsernameNotFoundException Si no encuentra el registro en la BD.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.buscarUsuarioPorUsername(username);
        usuario.orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario "+ username
                +" en la BD"));

        return usuario.map(MiUserDetails::new).get();

    } // din de la carga

} // fin de la clase de servicio
