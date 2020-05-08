package com.auth.jwt.app.service;

import com.auth.jwt.app.entity.Role;
import com.auth.jwt.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Clase que implementa los metodos de la interfaz {@link IRoleService} del servicio para los
 * roles.
 */
@Service
public class RoleService implements IRoleService{

    /**
     * Inyeccion para acceder a los metodos del repositorio
     */
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> obtenerTodosRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    @Override
    public Role buscarRolePorId(Integer idRole) {
        return roleRepository.findById(idRole).orElse(null);
    }
} // fin de la implementacion de la interfaz de servicios
