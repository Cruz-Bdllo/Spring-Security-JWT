package com.auth.jwt.app.security;

import com.auth.jwt.app.security.service.MiUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private MiUserDetailsService userDetailsService;


    @Bean
    public BCryptPasswordEncoder passEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Indicamos que queremos una autenticacion personalizada en este caso definimos el comportamiento
     * del <b>serDetailsService</b> en nuestra clase {@link MiUserDetailsService}, esto permite personalizar
     * la autenticacion.
     * Tambien indicamos que debe cifrar la contraseña que se cree o que se analice.
     * @param auth usado para indicar la autenticacion por medio de la BD.
     * @throws Exception si existe un problema con la autenticacion.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passEncoder());
    }

    // Establecemos que rutas y/o recursos estaran protegidos
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/registrarse")
                    .permitAll()
                    .antMatchers("/public")
                    .permitAll();
    }
} // fin de la clase de configuracion
