package com.auth.jwt.app.controller;

import com.auth.jwt.app.entity.Role;
import com.auth.jwt.app.entity.Usuario;
import com.auth.jwt.app.payload.AutenticacionLogin;
import com.auth.jwt.app.payload.AutenticacionResponse;
import com.auth.jwt.app.security.service.MiUserDetailsService;
import com.auth.jwt.app.security.utils.JwtUtil;
import com.auth.jwt.app.service.IRoleService;
import com.auth.jwt.app.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    // para inciar sesion
    @Autowired
    private AuthenticationManager authManager;


    @Autowired
    private MiUserDetailsService miUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarSesion(@RequestBody AutenticacionLogin autLogin) throws Exception{
        //autLogin.getPassword();
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(autLogin.getUsername(), autLogin.getPassword())
            );

        }catch (BadCredentialsException ex){
            System.out.println("errorrrr " +ex.getMessage());
            throw new Exception("Error en el username o contraseña " + ex.getMessage());
        } // fin de try~catch

        // Obtenemos los datos del usuario de la BD para construir el token
        final UserDetails userDetails = miUserDetailsService.loadUserByUsername(autLogin.getUsername());
        final String token = jwtUtil.creatToken(userDetails);

        // Regresamos el token
        return ResponseEntity.ok(new AutenticacionResponse(token));
        //return ResponseEntity.ok(autLogin.getUsername()+" "+autLogin.getPassword());
    } // fin para iniciar sesion

    @GetMapping("/home")
    public String userAuthenticated(){
        return "Welcome";
    }

} // fin del controlador home
