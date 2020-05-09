package com.auth.jwt.app.payload;

import java.io.Serializable;

// Ver si sirve o uso el de usuarios
public class AutenticacionRegistro implements Serializable {
    private static final long serialVersionUID = 1L;
    /* ~ Propiedades
    ==================================== */
    private String username;
    private String email;
    private String password;

    /* ~ Metodos
    ==================================== */
    public AutenticacionRegistro(){}

    public AutenticacionRegistro(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
