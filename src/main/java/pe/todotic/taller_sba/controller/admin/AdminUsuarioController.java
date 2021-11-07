package pe.todotic.taller_sba.controller.admin;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.todotic.taller_sba.model.Libro;
import pe.todotic.taller_sba.model.Usuario;
import pe.todotic.taller_sba.repo.UsuarioRepository;
import pe.todotic.taller_sba.webdto.UsuarioDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController {


    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AdminUsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public Page<Usuario> index(@PageableDefault(page = 0, size = 5) Pageable pageable){
        Page<Usuario> pageableUsuarios=  usuarioRepository.findAll(pageable);
        return pageableUsuarios;
    }

    @PostMapping
    public Usuario save(@RequestBody @Valid  UsuarioDto usuarioDto){
        Usuario usuario = new ModelMapper().map(usuarioDto,Usuario.class);
        return usuarioRepository.save(usuario);
    }

    @PutMapping("{id}")
    public Usuario update(@PathVariable Integer id,@RequestBody @Valid UsuarioDto usuarioDto){
       Usuario ususarioBase =  usuarioRepository.findById(id).get();
        ModelMapper mapper = new ModelMapper();
        mapper.map(usuarioDto,ususarioBase);
       return usuarioRepository.save(ususarioBase);
    }

    @GetMapping("{id}")
    Usuario get(@PathVariable Integer id){
        return usuarioRepository.findById(id).get();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id){
        Usuario usuario = usuarioRepository.getById(id);
        usuarioRepository.delete(usuario);
    }
}



