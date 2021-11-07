package pe.todotic.taller_sba.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.todotic.taller_sba.service.FileSystemStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final FileSystemStorageService fileSystemStorageService;

    @Autowired
    public AssetController(FileSystemStorageService fileSystemStorageService) {
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @GetMapping("{filename:.+}")
    ResponseEntity<Resource>  getArchivo(@PathVariable String filename) throws IOException {
        Resource resource =  fileSystemStorageService.loadAsResource(filename);
        String contenType = Files.probeContentType(resource.getFile().toPath());

        log.info("El tipo de contenido para {} es {}",filename,contenType);

        return ResponseEntity.ok().header("Content-Type",contenType).body(resource);
    }

    @PostMapping("/upload")
    Map<String, String> subiraArchivo(@RequestParam MultipartFile file){
        String rutaArchivo= fileSystemStorageService.store(file);

        String url = ServletUriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/api/assets/")
                .path(rutaArchivo).toUriString();

        Map<String, String> result = new HashMap();
        result.put("ruta",rutaArchivo);
        result.put("url",url);

        return result;
    }
}
