package pe.todotic.taller_sba.controller.admin;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.todotic.taller_sba.controller.BaseController;
import pe.todotic.taller_sba.model.Libro;
import pe.todotic.taller_sba.repo.LibroRepository;
import pe.todotic.taller_sba.webdto.LibroDto;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/libros")  //http://localhost:8080/libros
public class AdminLibroController extends BaseController {

    private final LibroRepository libroRepository;

    @Autowired
    public AdminLibroController(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @GetMapping
    Page<Libro> index(@PageableDefault(sort = "titulo",size = 5, direction = Sort.Direction.ASC) Pageable pageable){ // pageDefault es recomendable y la ordenacion
        Page<Libro> libros = libroRepository.findAll(pageable);

        libros.forEach(libro -> {
            libro.setUrlPortada(makeUrlBuilder(libro.getRutaPortada()));
            libro.setUrlArchivo(makeUrlBuilder(libro.getRutaArchivo()));
        });
        return libros;
    }

    @GetMapping("{id}")
    Libro get(@PathVariable Integer id){

        Libro libro = libroRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        String url = ServletUriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/api/assets/")
                .path(libro.getRutaPortada()).toUriString();

        libro.setUrlPortada(url);
        return libro;
    }

    @PostMapping
    Libro crear(@RequestBody  @Valid LibroDto libroDto){ //cuerpo de la solicitud http con json para serializar un json a una clase java
        Libro libro = new ModelMapper().map(libroDto,Libro.class);
        return   libroRepository.save(libro);
    }

    @PutMapping("{id}")
    Libro actualizar(@PathVariable  Integer id, @RequestBody @Valid LibroDto libroDto){
        Libro libroFromDb =  libroRepository.findById(id).get();

        ModelMapper mapper = new ModelMapper();
        mapper.map(libroDto,libroFromDb);  //mapeando de origen a destino

        return libroRepository.save(libroFromDb);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) // si no pongo eso retorna el codigo 500
    @DeleteMapping("{id}")
    void eliminar(@PathVariable Integer id){
        Libro libro = libroRepository.getById(id);
        libroRepository.delete(libro);
    }

}
