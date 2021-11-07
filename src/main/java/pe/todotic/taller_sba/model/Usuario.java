package pe.todotic.taller_sba.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer id;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;
    private String nombreCompleto;

    @NotBlank
    @Email
    private String email;

    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

    @Enumerated(EnumType.STRING)
    private Rol rol;


    private String password;

    @NotBlank(message = "el password es obligatorio")
    @Transient
    private String passwordPlain;

    @PrePersist
    void beforeSave(){
        this.nombreCompleto = nombres + apellidos ;
        this.fechaCreacion= LocalDateTime.now();
    }
    @PreUpdate
    void beforeUpdate(){
        this.nombreCompleto = nombres + apellidos ;
        this.fechaActualizacion= LocalDateTime.now();
    }

    public enum Rol {
        ADMIN,LECTOR
    }
}
