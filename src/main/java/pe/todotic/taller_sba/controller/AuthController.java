package pe.todotic.taller_sba.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.todotic.taller_sba.exception.BadRequestException;
import pe.todotic.taller_sba.model.Usuario;
import pe.todotic.taller_sba.repo.UsuarioRepository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registrar")
    void registrarUsuario(@RequestBody @Validated Usuario usuario){
       boolean emailExiste =  usuarioRepository.existsByEmail(usuario.getEmail());
       if(emailExiste){
           throw new BadRequestException("El email ya fue registrado para otro usuario");
       }

       String password = passwordEncoder.encode(usuario.getPasswordPlain());
       usuario.setPassword(password);
       usuario.setRol(Usuario.Rol.LECTOR);

       usuarioRepository.save(usuario);
    }

    @GetMapping("/verificar-email")
    Map<String, Boolean> verificarEmail(@RequestParam String email){
        boolean emailExiste =  usuarioRepository.existsByEmail(email);
        Map<String, Boolean> result= new HashMap<>();
        result.put("exists",emailExiste);
        return  result;
    }
}
