package com.auth.jwt.app.service;

import com.auth.jwt.app.entity.Usuario;

import java.util.List;

/**
 * Permite envolver los metodos del la interfaz del repositorio que extiende
 * de {@link org.springframework.data.jpa.repository.JpaRepository} renombrando
 * estos metodos a unos mas comodos, ademas de no trabajar directamente sobre el
 * repositorio.
 */
public interface IUsuarioService {

    /**
     * Envuelve al metodo <b>findAll</b> de <b>JpaRepository</b>
     * el cual devuelve todos los registros de la tabla <b>usuarios</b>
     * @return List(Usuario)
     */
    List<Usuario> buscarTodosUsuarios();

    /**
     * Envuelve al metodo <b>findById</b> de <b>JpaRepository</b>
     * el cual retorna el registro buscado por su id.
     * @param idUsuario
     * @return Usuario
     */
    Usuario buscarUsuarioPorId(Integer idUsuario);

    /**
     * Envuelve el metodo de la consulta personalizada en el repositorio {@link com.auth.jwt.app.repository.UsuarioRepository}
     * <b>buscarUsuarioPorUsername</b> que retorna un registro de la BD por su username.
     * @param username
     * @return Usuario
     */
    Usuario buscarUsuarioPorUsername(String username);

    /**
     * Envuelve el metodo de la consulta personalizada en el repositorio {@link com.auth.jwt.app.repository.UsuarioRepository}
     * <b>buscarUsuarioPorCorreo</b> que retorna un registro de la BD por su correo.
     * @param correo
     * @return Usuario
     */
    Usuario buscarUsuarioPorCorreo(String correo);

    /**
     * Envuelve al metodo <b>save</b> de <b>JpaRepository</b>
     * que guarda en la BD al usuario que se pasa atraves del su parametro
     * @param usuario
     */
    void guardarUsuario(Usuario usuario);

    /**
     * Envuelve al metodo <b>deleteById</b> de <b>JpaRepository</b>
     * este elimina un registro de la BD por medio de su id.
     * @param idUsuario
     */
    void eliminarUsuarioPorId(Integer idUsuario);

} // fin de la interface de servicio
