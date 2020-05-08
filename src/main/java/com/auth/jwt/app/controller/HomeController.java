package com.auth.jwt.app.controller;

import com.auth.jwt.app.entity.Role;
import com.auth.jwt.app.entity.Usuario;
import com.auth.jwt.app.repository.RoleRepository;
import com.auth.jwt.app.service.IRoleService;
import com.auth.jwt.app.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/public")
    public String homePublic(){
        return "Pagina de inicio al publico";
    } // fin de la peticion

    // El formulario registrar contara con los campos: username, correo, password
    @PostMapping("/registrarse")
    public ResponseEntity<?> registrarse(@RequestBody Usuario usuario){
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asignar role de user
        Role role = roleService.buscarRolePorId(3);
        usuario.agregarRoleALista(role);
        usuarioService.guardarUsuario(usuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    } // fin de la pagina de registro

} // fin del controlador home
