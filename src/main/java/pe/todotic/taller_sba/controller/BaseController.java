package pe.todotic.taller_sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @Autowired
    private HttpServletRequest request;

    public String makeUrlBuilder(String ruta){
        if(ruta !=null){

            String host = request.getRequestURL().toString().replace(request.getRequestURI(),""); // obtengo el dominio y reemplazo el resto de la URI conn espacio en blanco

            return  ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("/api/assets")
                    .path(ruta)
                    .toUriString();
        }
        return  null;
    }
}
