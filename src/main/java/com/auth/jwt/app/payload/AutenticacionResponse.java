package com.auth.jwt.app.payload;

import java.io.Serializable;

public class AutenticacionResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;

    public AutenticacionResponse(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
