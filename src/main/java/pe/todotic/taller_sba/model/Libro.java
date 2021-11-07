package pe.todotic.taller_sba.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlibro")
    private Integer id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String slug;

    @NotBlank
    private String rutaPortada;

    @NotBlank
    private String rutaArchivo;

    @NotBlank
    private String descripcion;

    @NotNull
    @PositiveOrZero
    private Float precio;


    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

    @Transient
    private String urlPortada;

    @Transient
    private String urlArchivo;

    @PrePersist
    void asigneDateCreation(){
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void asigneDateUpdate(){
        this.fechaActualizacion = LocalDateTime.now();
    }
}

