package com.auth.jwt.app.payload;

import java.io.Serializable;

public class AutenticacionLogin implements Serializable {
    private static final long serialVersionUID = 1L;
    /* ~ Propiedades
    ==================================== */
    private String username;
    private String password;

    /* ~ Metodos
    ==================================== */
    public AutenticacionLogin() {}

    public AutenticacionLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} // fin de la clase
