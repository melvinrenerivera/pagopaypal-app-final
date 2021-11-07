package pe.todotic.taller_sba.webdto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class LibroDto {
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
}
