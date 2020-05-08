package com.auth.jwt.app.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Esta clase representa a la tabla de la BD llamada <b>roles</b>
 * en la cual las propiedades definidas aqui seran mapeadas a la tabla.
 * y en dicha tabla se encuentran los tipos de acceso al siste <b>(ADMIN, USER, PUBLIC)</b>.
 */

@Entity
@Table(name = "roles")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    /* ~ Propiedades
    ==================================== */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRole;

    @Column(name = "nombre_role")
    private String nombreRole;

    private String descripcion;

    /* ~ Metodos
    ==================================== */

    public Role() {
    }

    public Integer getIdRole() {
        return idRole;
    }

    public String getNombreRole() {
        return nombreRole;
    }

    public void setNombreRole(String nombreRole) {
        this.nombreRole = nombreRole;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
