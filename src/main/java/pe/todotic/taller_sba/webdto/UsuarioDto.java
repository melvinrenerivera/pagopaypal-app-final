package pe.todotic.taller_sba.webdto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class UsuarioDto {

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;
    private String nombreCompleto;

    @NotBlank
    @Email
    private String email;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    @NotBlank(message = "el password es obligatorio")
    private String password;

}
